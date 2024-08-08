package com.github.akrtkv.semd_demo.util;

import com.github.akrtkv.semd_demo.dto.recipe.FullRecipe;
import com.github.akrtkv.semd_demo.dto.recipe.Recipe;
import com.github.akrtkv.semd_demo.dto.semd.*;
import com.github.akrtkv.semd_demo.dto.semd.address.Type;
import com.github.akrtkv.semd_demo.dto.semd.fias.FiasAddress;
import com.github.akrtkv.semd_demo.dto.semd.identity.POCDMT000040IdentityDoc;
import com.github.akrtkv.semd_demo.dto.semd.identity.POCDMT000040InsurancePolicy;
import com.github.akrtkv.semd_demo.dto.semd.identity.POCDMT000040Props;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.github.akrtkv.semd_demo.util.Constants.*;

@Component
public class ObjectCreator {

    private final Converter converter;

    @Autowired
    public ObjectCreator(Converter converter) {
        this.converter = converter;
    }

    public String createSemdDrugXml(FullRecipe recipe, int version) {
        POCDMT000040ClinicalDocument clinicalDocument = createSemdClinicalDocumentCommonData(recipe, version, true);

        if (version > 3) {
            // Сведения о документируемом событии
            clinicalDocument.setDocumentationOf(new POCDMT000040DocumentationOf());
            clinicalDocument.getDocumentationOf().setServiceEvent(new POCDMT000040ServiceEvent());
            clinicalDocument.getDocumentationOf().getServiceEvent().setCode(new POCDMT000040ServiceEvent.Code());
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCode("58");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCodeSystem("1.2.643.5.1.13.13.99.2.726");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCodeSystemName("Типы документированных событий");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCodeSystemVersion("3.1");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setDisplayName("Формирование рецепта на лекарственный препарат");
            clinicalDocument.getDocumentationOf().getServiceEvent().setEffectiveTime(new POCDMT000040ServiceEvent.EffectiveTime());
            clinicalDocument.getDocumentationOf().getServiceEvent().getEffectiveTime().setValue(recipe.getCreateDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
        }

        // Секция назначение лекарственного препарата\специализированного продукта лечебного питания
        var recipeSection = new POCDMT000040Component3();
        recipeSection.setSection(new POCDMT000040Section());

        // Код секции
        recipeSection.getSection().setCode(new POCDMT000040Section.Code());
        recipeSection.getSection().getCode().setCode(RECIPE);
        recipeSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
        recipeSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
        recipeSection.getSection().getCode().setCodeSystemVersion(version == 3 ? "1.19" : "2.1");
        recipeSection.getSection().getCode().setDisplayName(RECIPE_NAME);

        recipeSection.getSection().setTitle(PREFERENTIAL_RECIPE_SECTION_TITLE);

        // Наполнение секции
        recipeSection.getSection().setText(new StrucDocText());
        var strucDocTable = new StrucDocTable();
        var strucDocTbody = new StrucDocTbody();
        var strucDocThead = new StrucDocThead();

        var header = new StrucDocTr();
        var thHeaderDrug = new StrucDocTh();
        var thHeaderDose = new StrucDocTh();
        thHeaderDrug.getContent().add(APPOINTED);
        thHeaderDose.getContent().add(DOSING_NAME);
        header.getThOrTd().add(thHeaderDrug);
        header.getThOrTd().add(thHeaderDose);
        strucDocThead.getTr().add(header);

        var body = new StrucDocTr();
        var thBodyDrug = new StrucDocTd();
        var thBodyDose = new StrucDocTd();
        thBodyDrug.getContent().add(recipe.getDrug().getName() + (recipe.getDrug().getTradeName() != null ? " (торговое наименование " + recipe.getDrug().getTradeName() + ")" : ""));
        if (recipe.getDrug().getDose() != null) {
            thBodyDose.getContent().add(recipe.getDrug().getDose().toString());
        }
        body.getThOrTd().add(thBodyDrug);
        body.getThOrTd().add(thBodyDose);
        strucDocTbody.getTr().add(body);

        var recipeSectionEntry = new POCDMT000040Entry();
        recipeSectionEntry.setSubstanceAdministration(new POCDMT000040SubstanceAdministration());
        recipeSectionEntry.getSubstanceAdministration().getClassCode().add(SBADM);
        recipeSectionEntry.getSubstanceAdministration().setMoodCode(XDocumentSubstanceMood.RQO);

        recipeSectionEntry.getSubstanceAdministration().setCode(new POCDMT000040SubstanceAdministration.Code());
        recipeSectionEntry.getSubstanceAdministration().getCode().getNullFlavor().add("NI");

        // Длительность приема препарата
        if (recipe.getDrug().getDurationDays() != null) {
            var ivlts = new IVLTS();
            var width = new PQ();
            width.setValue(recipe.getDrug().getDurationDays().toString());
            width.setUnit("d");
            var pqr = new PQR();
            pqr.setValue(recipe.getDrug().getDurationDays().toString());
            pqr.setCode("24");
            pqr.setDisplayName("сут");
            pqr.setCodeSystem(TRANSLATION_CODE_SYSTEM);
            pqr.setCodeSystemVersion("3.2");
            pqr.setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
            width.getTranslation().add(pqr);
            ivlts.getRest().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, "width"), PQ.class, width));
            recipeSectionEntry.getSubstanceAdministration().setEffectiveTime(ivlts);
        }

        // Путь введения препарата
        if (recipe.getDrug().getRouteCode() != null && recipe.getDrug().getRouteName() != null) {
            recipeSectionEntry.getSubstanceAdministration().setRouteCode(new POCDMT000040SubstanceAdministration.RouteCode());
            recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCode(recipe.getDrug().getRouteCode().toString());
            recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystem(ROUTE_CODE_SYSTEM);
            recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemName(ROUTE_CODE_SYSTEM_NAME);
            recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemVersion("2.3");
            recipeSectionEntry.getSubstanceAdministration().getRouteCode().setDisplayName(recipe.getDrug().getRouteName());
        }

        // Назначенный препарат\специализированный продукт лечебного питания
        recipeSectionEntry.getSubstanceAdministration().setConsumable(new POCDMT000040Consumable());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().setTypeCode("CSM");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().setManufacturedProduct(new POCDMT000040ManufacturedProduct());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setClassCode("MANU");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setManufacturedMaterial(new POCDMT000040Material());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setClassCode("MMAT");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setDeterminerCode("KIND");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setCode(new POCDMT000040Material.Code());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCode(recipe.getDrug().getCodeMnn());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystem(MATERIAL_CODE_SYSTEM);
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemName(MATERIAL_CODE_SYSTEM_NAME);
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemVersion("5.36");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setDisplayName(recipe.getDrug().getName());
        if (recipe.getDrug().getTradeName() != null) {
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setName(recipe.getDrug().getTradeName());
        }

        // Количество назначенных доз
        var doseEntry = new POCDMT000040EntryRelationship();
        doseEntry.setTypeCode("COMP");
        doseEntry.setObservation(new POCDMT000040Observation());
        doseEntry.getObservation().getClassCode().add("OBS");
        doseEntry.getObservation().setMoodCode("EVN");
        doseEntry.getObservation().setCode(new POCDMT000040Observation.Code());
        doseEntry.getObservation().getCode().setCode("6011");
        doseEntry.getObservation().getCode().setCodeSystem(OBSERVATION_CODE_SYSTEM);
        doseEntry.getObservation().getCode().setCodeSystemName(OBSERVATION_CODE_SYSTEM_NAME);
        doseEntry.getObservation().getCode().setCodeSystemVersion("1.75");
        doseEntry.getObservation().getCode().setDisplayName(DOSING_NAME);

        if (recipe.getDrug().getDose() != null) {
            var pq = new PQ();
            pq.setValue(recipe.getDrug().getDose().toString());
            pq.setUnit("U");
            var translation = new PQR();
            translation.setValue(recipe.getDrug().getDose().toString());
            translation.setCode("128");
            translation.setDisplayName(UNIT_NAME);
            translation.setCodeSystem(TRANSLATION_CODE_SYSTEM);
            translation.setCodeSystemVersion("3.2");
            translation.setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
            pq.getTranslation().add(translation);
            doseEntry.getObservation().setValue(pq);
            recipeSectionEntry.getSubstanceAdministration().getEntryRelationship().add(doseEntry);
        }

        recipeSection.getSection().getEntry().add(recipeSectionEntry);

        strucDocTable.getTbody().add(strucDocTbody);
        strucDocTable.setThead(strucDocThead);
        recipeSection.getSection().getText().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, TABLE), StrucDocTable.class, strucDocTable));
        clinicalDocument.getComponent().getStructuredBody().getComponent().add(recipeSection);
        return converter.convertObjectToXmlString(clinicalDocument, POCDMT000040ClinicalDocument.class, true);
    }

    public String createSemdMedicalProductXml(FullRecipe recipe, int version) {
        var clinicalDocument = createSemdClinicalDocumentCommonData(recipe, version, true);

        if (version > 3) {
            // Сведения о документируемом событии
            clinicalDocument.setDocumentationOf(new POCDMT000040DocumentationOf());
            clinicalDocument.getDocumentationOf().setServiceEvent(new POCDMT000040ServiceEvent());
            clinicalDocument.getDocumentationOf().getServiceEvent().setCode(new POCDMT000040ServiceEvent.Code());
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCode("60");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCodeSystem("1.2.643.5.1.13.13.99.2.726");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCodeSystemName("Типы документированных событий");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCodeSystemVersion("3.1");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setDisplayName("Формирование рецепта на медицинское изделие");
            clinicalDocument.getDocumentationOf().getServiceEvent().setEffectiveTime(new POCDMT000040ServiceEvent.EffectiveTime());
            clinicalDocument.getDocumentationOf().getServiceEvent().getEffectiveTime().setValue(recipe.getCreateDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
        }

        // Секция назначение лекарственного препарата\специализированного продукта лечебного питания
        var recipeSection = new POCDMT000040Component3();
        recipeSection.setSection(new POCDMT000040Section());

        // Код секции
        recipeSection.getSection().setCode(new POCDMT000040Section.Code());
        recipeSection.getSection().getCode().setCode(RECIPE);
        recipeSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
        recipeSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
        recipeSection.getSection().getCode().setCodeSystemVersion(version == 3 ? "1.19" : "2.1");
        recipeSection.getSection().getCode().setDisplayName(RECIPE_NAME);

        recipeSection.getSection().setTitle(PREFERENTIAL_RECIPE_SECTION_TITLE);

        // Наполнение секции
        recipeSection.getSection().setText(new StrucDocText());
        var strucDocTable = new StrucDocTable();
        var strucDocTbody = new StrucDocTbody();

        var strucDocTrName = new StrucDocTr();
        var thHeaderDrug = new StrucDocTh();
        var tdHeaderDose = new StrucDocTd();
        thHeaderDrug.getContent().add("Наименование медицинского изделия");
        tdHeaderDose.getContent().add(recipe.getMedicalProduct().getName());
        strucDocTrName.getThOrTd().add(thHeaderDrug);
        strucDocTrName.getThOrTd().add(tdHeaderDose);
        strucDocTbody.getTr().add(strucDocTrName);

        var strucDocTrAmount = new StrucDocTr();
        var thAmount = new StrucDocTh();
        var tdAmount = new StrucDocTd();
        thAmount.getContent().add("Количество");
        tdAmount.getContent().add(recipe.getMedicalProduct().getAmount().toString());
        strucDocTrAmount.getThOrTd().add(thAmount);
        strucDocTrAmount.getThOrTd().add(tdAmount);
        strucDocTbody.getTr().add(strucDocTrAmount);

        // Кодирование льготного рецепта
        var entry = new POCDMT000040Entry();
        entry.setSupply(new POCDMT000040Supply());
        entry.getSupply().setClassCode(ActClassSupply.SPLY);
        entry.getSupply().setMoodCode(XDocumentSubstanceMood.RQO);

        entry.getSupply().setCode(new POCDMT000040Supply.Code());
        entry.getSupply().getCode().getNullFlavor().add("NI");

        // Текст рецепта
        entry.getSupply().setText(recipe.getMedicalProduct().getName());
        recipeSection.getSection().getEntry().add(entry);

        entry.getSupply().setQuantity(new BXITIVLPQ());
        entry.getSupply().getQuantity().setValue(recipe.getMedicalProduct().getAmount().toString());
        var translation = new PQR();
        translation.setValue(recipe.getMedicalProduct().getAmount().toString());
        translation.setCode("128");
        translation.setCodeSystem("1.2.643.5.1.13.13.11.1358");
        translation.setCodeSystemVersion("3.12");
        translation.setCodeSystemName("Единицы измерения");
        translation.setDisplayName("Ед");
        entry.getSupply().getQuantity().getTranslation().add(translation);

        entry.getSupply().setProduct(new POCDMT000040Product());
        entry.getSupply().getProduct().setTypeCode("PRD");
        entry.getSupply().getProduct().setManufacturedProduct(new POCDMT000040ManufacturedProduct());
        entry.getSupply().getProduct().getManufacturedProduct().setClassCode("MANU");
        entry.getSupply().getProduct().getManufacturedProduct().setManufacturedMaterial(new POCDMT000040Material());
        entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().setClassCode("MMAT");
        entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().setDeterminerCode("KIND");
        entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().setCode(new POCDMT000040Material.Code());
        entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().setCode(recipe.getMedicalProduct().getCode());
        entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystem("1.2.643.5.1.13.13.99.2.604");
        entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemName("ФРЛЛО. Справочник медицинских изделий по классификации Казначейства России");
        entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemVersion("1.213");
        entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().setDisplayName(recipe.getMedicalProduct().getName());
        entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().setName(recipe.getMedicalProduct().getName());

        strucDocTable.getTbody().add(strucDocTbody);
        recipeSection.getSection().getText().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, TABLE), StrucDocTable.class, strucDocTable));
        clinicalDocument.getComponent().getStructuredBody().getComponent().add(recipeSection);
        return converter.convertObjectToXmlString(clinicalDocument, POCDMT000040ClinicalDocument.class, true);
    }

    public String createSemdHealthFoodXml(FullRecipe recipe, int version) {
        var clinicalDocument = createSemdClinicalDocumentCommonData(recipe, version, true);

        if (version > 3) {
            // Сведения о документируемом событии
            clinicalDocument.setDocumentationOf(new POCDMT000040DocumentationOf());
            clinicalDocument.getDocumentationOf().setServiceEvent(new POCDMT000040ServiceEvent());
            clinicalDocument.getDocumentationOf().getServiceEvent().setCode(new POCDMT000040ServiceEvent.Code());
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCode("59");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCodeSystem("1.2.643.5.1.13.13.99.2.726");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCodeSystemName("Типы документированных событий");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setCodeSystemVersion("3.1");
            clinicalDocument.getDocumentationOf().getServiceEvent().getCode().setDisplayName("Формирование рецепта на специализированный продукт лечебного питания");
            clinicalDocument.getDocumentationOf().getServiceEvent().setEffectiveTime(new POCDMT000040ServiceEvent.EffectiveTime());
            clinicalDocument.getDocumentationOf().getServiceEvent().getEffectiveTime().setValue(recipe.getCreateDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
        }

        // Секция назначение лекарственного препарата\специализированного продукта лечебного питания
        var recipeSection = new POCDMT000040Component3();
        recipeSection.setSection(new POCDMT000040Section());

        // Код секции
        recipeSection.getSection().setCode(new POCDMT000040Section.Code());
        recipeSection.getSection().getCode().setCode(RECIPE);
        recipeSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
        recipeSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
        recipeSection.getSection().getCode().setCodeSystemVersion(version == 3 ? "1.19" : "2.1");
        recipeSection.getSection().getCode().setDisplayName(RECIPE_NAME);

        recipeSection.getSection().setTitle(PREFERENTIAL_RECIPE_SECTION_TITLE);

        // Наполнение секции
        recipeSection.getSection().setText(new StrucDocText());
        var strucDocTable = new StrucDocTable();
        var strucDocTbody = new StrucDocTbody();
        var strucDocThead = new StrucDocThead();

        var header = new StrucDocTr();
        var thHeaderDrug = new StrucDocTh();
        var thHeaderDose = new StrucDocTh();
        thHeaderDrug.getContent().add(APPOINTED);
        thHeaderDose.getContent().add(DOSING_NAME);
        header.getThOrTd().add(thHeaderDrug);
        header.getThOrTd().add(thHeaderDose);
        strucDocThead.getTr().add(header);

        var body = new StrucDocTr();
        var thBodyDrug = new StrucDocTd();
        var thBodyDose = new StrucDocTd();
        thBodyDrug.getContent().add(recipe.getHealthFood().getName());
        thBodyDose.getContent().add(recipe.getHealthFood().getDose().toString());
        body.getThOrTd().add(thBodyDrug);
        body.getThOrTd().add(thBodyDose);
        strucDocTbody.getTr().add(body);

        var recipeSectionEntry = new POCDMT000040Entry();
        recipeSectionEntry.setSubstanceAdministration(new POCDMT000040SubstanceAdministration());
        recipeSectionEntry.getSubstanceAdministration().getClassCode().add(SBADM);
        recipeSectionEntry.getSubstanceAdministration().setMoodCode(XDocumentSubstanceMood.RQO);

        recipeSectionEntry.getSubstanceAdministration().setCode(new POCDMT000040SubstanceAdministration.Code());
        recipeSectionEntry.getSubstanceAdministration().getCode().getNullFlavor().add("NI");

        // Длительность приема препарата
        var ivlts = new IVLTS();
        var width = new PQ();
        width.setValue(recipe.getHealthFood().getDurationDays().toString());
        width.setUnit("d");
        var pqr = new PQR();
        pqr.setValue(recipe.getHealthFood().getDurationDays().toString());
        pqr.setCode("24");
        pqr.setDisplayName("сут");
        pqr.setCodeSystem(TRANSLATION_CODE_SYSTEM);
        pqr.setCodeSystemVersion("3.2");
        pqr.setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
        width.getTranslation().add(pqr);
        ivlts.getRest().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, "width"), PQ.class, width));
        recipeSectionEntry.getSubstanceAdministration().setEffectiveTime(ivlts);

        // Путь введения препарата
        recipeSectionEntry.getSubstanceAdministration().setRouteCode(new POCDMT000040SubstanceAdministration.RouteCode());
        recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCode(recipe.getHealthFood().getRouteCode().toString());
        recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystem(ROUTE_CODE_SYSTEM);
        recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemName(ROUTE_CODE_SYSTEM_NAME);
        recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemVersion("2.3");
        recipeSectionEntry.getSubstanceAdministration().getRouteCode().setDisplayName(recipe.getHealthFood().getRouteName());

        // Назначенный препарат\специализированный продукт лечебного питания
        recipeSectionEntry.getSubstanceAdministration().setConsumable(new POCDMT000040Consumable());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().setTypeCode("CSM");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().setManufacturedProduct(new POCDMT000040ManufacturedProduct());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setClassCode("MANU");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setManufacturedMaterial(new POCDMT000040Material());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setClassCode("MMAT");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setDeterminerCode("KIND");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setCode(new POCDMT000040Material.Code());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCode(recipe.getHealthFood().getCode());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystem("1.2.643.5.1.13.13.99.2.603");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemName("ФРЛЛО. Справочник специализированного питания");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemVersion("1.4");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setDisplayName(recipe.getHealthFood().getDescription());

        // Количество назначенных доз
        var doseEntry = new POCDMT000040EntryRelationship();
        doseEntry.setTypeCode("COMP");
        doseEntry.setObservation(new POCDMT000040Observation());
        doseEntry.getObservation().getClassCode().add("OBS");
        doseEntry.getObservation().setMoodCode("EVN");
        doseEntry.getObservation().setCode(new POCDMT000040Observation.Code());
        doseEntry.getObservation().getCode().setCode("6011");
        doseEntry.getObservation().getCode().setCodeSystem(OBSERVATION_CODE_SYSTEM);
        doseEntry.getObservation().getCode().setCodeSystemName(OBSERVATION_CODE_SYSTEM_NAME);
        doseEntry.getObservation().getCode().setCodeSystemVersion(version == 3 ? "1.75" : "2.1");
        doseEntry.getObservation().getCode().setDisplayName(DOSING_NAME);

        var pq = new PQ();
        pq.setValue(recipe.getHealthFood().getDose().toString());
        pq.setUnit("U");
        var translation = new PQR();
        translation.setValue(recipe.getHealthFood().getDose().toString());
        translation.setCode("128");
        translation.setDisplayName(UNIT_NAME);
        translation.setCodeSystem(TRANSLATION_CODE_SYSTEM);
        translation.setCodeSystemVersion("3.2");
        translation.setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
        pq.getTranslation().add(translation);
        doseEntry.getObservation().setValue(pq);
        recipeSectionEntry.getSubstanceAdministration().getEntryRelationship().add(doseEntry);
        recipeSection.getSection().getEntry().add(recipeSectionEntry);

        strucDocTable.getTbody().add(strucDocTbody);
        strucDocTable.setThead(strucDocThead);
        recipeSection.getSection().getText().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, TABLE), StrucDocTable.class, strucDocTable));
        clinicalDocument.getComponent().getStructuredBody().getComponent().add(recipeSection);
        return converter.convertObjectToXmlString(clinicalDocument, POCDMT000040ClinicalDocument.class, true);
    }

    private POCDMT000040ClinicalDocument createSemdClinicalDocumentCommonData(Recipe recipe, int version, boolean preferential) {
        var clinicalDocument = new POCDMT000040ClinicalDocument();

        // Область применения документа (Страна)
        clinicalDocument.setRealmCode(new POCDMT000040ClinicalDocument.RealmCode());
        clinicalDocument.getRealmCode().setCode("RU");

        // Указатель на использование CDA R2
        clinicalDocument.setTypeId(new POCDMT000040ClinicalDocument.TypeId());
        clinicalDocument.getTypeId().setRoot(CODING_SCHEME_VALUE);
        clinicalDocument.getTypeId().setExtension("POCD_MT000040");

        // Идентификатор Шаблона документа "Льготный рецепт на лекарственный препарат, изделие медицинского назначения и специализированный продукт лечебного питания. Третий уровень формализации."
        // по справочнику "Справочник шаблонов CDA документов" (OID: 1.2.643.5.1.13.13.99.2.267)
        clinicalDocument.setTemplateId(new POCDMT000040ClinicalDocument.TemplateId());
        if (version == 3) {
            clinicalDocument.getTemplateId().setRoot("1.2.643.5.1.13.13.14.37.9.3");
        } else if (version == 4) {
            clinicalDocument.getTemplateId().setRoot("1.2.643.5.1.13.13.14.37.9.4");
        } else if (version == 2 && !preferential) {
            clinicalDocument.getTemplateId().setRoot("1.2.643.5.1.13.13.14.86.9.2");
        }

        // Уникальный идентификатор документа
        // по правилу: root = OID_медицинской_организации.100.НомерМИС.НомерЭкзМИС.51 extension = идентификатор документа
        clinicalDocument.setId(new POCDMT000040ClinicalDocument.Id());
        clinicalDocument.getId().setRoot(recipe.getProviderOrganization().getOid() + ".100.1.1.51");
        clinicalDocument.getId().setExtension(recipe.getMisId());

        // Тип документа
        if (preferential) {
            clinicalDocument.setCode(new POCDMT000040ClinicalDocument.Code());
            clinicalDocument.getCode().setCode("37");
            clinicalDocument.getCode().setCodeSystem("1.2.643.5.1.13.13.11.1522");
            clinicalDocument.getCode().setCodeSystemName("Виды медицинской документации");
            clinicalDocument.getCode().setCodeSystemVersion("5.15");
            clinicalDocument.getCode().setDisplayName(PREFERENTIAL_RECIPE_TITLE);
        } else {
            clinicalDocument.setCode(new POCDMT000040ClinicalDocument.Code());
            clinicalDocument.getCode().setCode("86");
            clinicalDocument.getCode().setCodeSystem("1.2.643.5.1.13.13.11.1522");
            clinicalDocument.getCode().setCodeSystemName("Виды медицинской документации");
            clinicalDocument.getCode().setCodeSystemVersion("5.15");
            clinicalDocument.getCode().setDisplayName(RECIPE_TITLE);
        }

        // Заголовок документа
        clinicalDocument.setTitle(recipe.getName());

        // Дата создания документа (Должен быть с точностью до дня, но следует быть с точностью до минут)
        clinicalDocument.setEffectiveTime(new POCDMT000040ClinicalDocument.EffectiveTime());
        clinicalDocument.getEffectiveTime().setValue(recipe.getCreateDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));

        // Уровень конфиденциальности медицинского документа
        clinicalDocument.setConfidentialityCode(new POCDMT000040ClinicalDocument.ConfidentialityCode());
        clinicalDocument.getConfidentialityCode().setCode("N");
        clinicalDocument.getConfidentialityCode().setCodeSystem("1.2.643.5.1.13.13.99.2.285");
        clinicalDocument.getConfidentialityCode().setCodeSystemVersion("1.2");
        clinicalDocument.getConfidentialityCode().setCodeSystemName("Уровень конфиденциальности медицинского документа");
        clinicalDocument.getConfidentialityCode().setDisplayName("Обычный");

        // Язык документа
        clinicalDocument.setLanguageCode(new POCDMT000040ClinicalDocument.LanguageCode());
        clinicalDocument.getLanguageCode().setCode(RU_RU);

        // Уникальный идентификатор документа
        // по правилу: root = OID_медицинской_организации.100.НомерМИС.НомерЭкзМИС.50 extension = идентификатор набора версий документа
        clinicalDocument.setSetId(new POCDMT000040ClinicalDocument.SetId());
        clinicalDocument.getSetId().setRoot(recipe.getProviderOrganization().getOid() + ".100.1.1.50");
        clinicalDocument.getSetId().setExtension(recipe.getVersionId().toString());

        // Номер версии данного документа
        clinicalDocument.setVersionNumber(new POCDMT000040ClinicalDocument.VersionNumber());
        clinicalDocument.getVersionNumber().setValue(recipe.getVersionId().toString());

        // Данные о пациенте
        clinicalDocument.setRecordTarget(new POCDMT000040RecordTarget());
        clinicalDocument.getRecordTarget().setPatientRole(new POCDMT000040PatientRole());
        clinicalDocument.getRecordTarget().getPatientRole().setPatient(new POCDMT000040Patient());
        clinicalDocument.getRecordTarget().getPatientRole().setProviderOrganization(new POCDMT000040OrganizationPatient());

        // Уникальный идентификатор пациента в МИС
        // по правилу: root = OID_медицинской_организации.100.НомерМИС.НомерЭкзМИС.10 extension = идентификатор пациента
        var patientId = new IIIdPatient();
        patientId.setRoot(recipe.getProviderOrganization().getOid() + ".100.1.1.10");
        patientId.setExtension(recipe.getPatient().getMisId());
        clinicalDocument.getRecordTarget().getPatientRole().getId().add(patientId);

        // СНИЛС пациента
        var patientSnils = new IIIdPatient();
        patientSnils.setRoot(SNILS_ROOT);
        patientSnils.setExtension(recipe.getPatient().getSnils());
        clinicalDocument.getRecordTarget().getPatientRole().getId().add(patientSnils);

        // Документ, удостоверяющий личность получателя, серия, номер, кем выдан
        // Тип документа
        clinicalDocument.getRecordTarget().getPatientRole().setIdentityDoc(new POCDMT000040IdentityDoc());
        if (recipe.getPatient().getIdentityDoc() != null) {
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setIdentityCardType(new POCDMT000040IdentityDoc.IdentityCardType());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardType().setCode("1");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardType().setCodeSystem("1.2.643.5.1.13.13.99.2.48");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardType().setCodeSystemName("Документы, удостоверяющие личность");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardType().setCodeSystemVersion("5.1");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardType().setDisplayName("Паспорт гражданина Российской Федерации");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setSeries(recipe.getPatient().getIdentityDoc().getSeries());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setNumber(recipe.getPatient().getIdentityDoc().getNumber());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setIssueOrgCode(recipe.getPatient().getIdentityDoc().getIssueOrgCode());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setIssueOrgName(recipe.getPatient().getIdentityDoc().getIssueOrgName());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setIssueDate(new POCDMT000040IdentityDoc.IssueDate());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIssueDate().setValue(recipe.getPatient().getIdentityDoc().getIssueDate().format(DateTimeFormatter.ofPattern(IEMK_DATE_FORMAT)));
            clinicalDocument.getRecordTarget().getPatientRole().setInsurancePolicy(new POCDMT000040InsurancePolicy());
        } else {
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getNullFlavor().add("NI");
        }

        // Полис ОМС
        clinicalDocument.getRecordTarget().getPatientRole().setInsurancePolicy(new POCDMT000040InsurancePolicy());
        clinicalDocument.getRecordTarget().getPatientRole().getInsurancePolicy().getNullFlavor().add("NI");

        // Адрес постоянной регистрации пациента
        var patientAddress = new ADPatient();
        if (recipe.getPatient().getAddress() != null) {
            patientAddress.setType(new Type());
            patientAddress.getType().setCode("1");
            patientAddress.getType().setCodeSystem("1.2.643.5.1.13.13.11.1504");
            patientAddress.getType().setCodeSystemName("Тип адреса пациента");
            patientAddress.getType().setCodeSystemVersion("1.3");
            patientAddress.getType().setDisplayName("Адрес по месту жительства (постоянной регистрации)");
            patientAddress.setStateCode(new CDAddressStateCode());
            patientAddress.getStateCode().setCode(recipe.getPatient().getAddress().getRegionCode());
            patientAddress.getStateCode().setCodeSystem(STATE_CODE_SYSTEM);
            patientAddress.getStateCode().setCodeSystemName(STATE_CODE_SYSTEM_NAME);
            patientAddress.getStateCode().setCodeSystemVersion("6.3");
            patientAddress.getStateCode().setDisplayName(recipe.getPatient().getAddress().getRegionName());
            patientAddress.setPostalCode(new AdxpPostalCode());
            patientAddress.getPostalCode().getNullFlavor().add("NI");
            patientAddress.setStreetAddressLine(recipe.getPatient().getAddress().getFullAddress());
            patientAddress.setAddress(new FiasAddress());
            if (recipe.getPatient().getAddress().getAoGuid() != null && recipe.getPatient().getAddress().getHouseGuid() != null) {
                patientAddress.getAddress().setAOGUID(recipe.getPatient().getAddress().getAoGuid());
                patientAddress.getAddress().setHOUSEGUID(recipe.getPatient().getAddress().getHouseGuid());
            } else {
                patientAddress.getAddress().getNullFlavor().add("NI");
            }
        } else {
            patientAddress.getNullFlavor().add("NI");
        }
        clinicalDocument.getRecordTarget().getPatientRole().getAddr().add(patientAddress);

        // ФИО, пол, ДР
        clinicalDocument.getRecordTarget().getPatientRole().getPatient().setName(new PN());
        clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, FAMILY), String.class, recipe.getPatient().getLastName()));
        clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, GIVEN), String.class, recipe.getPatient().getFirstName()));
        if (recipe.getPatient().getMiddleName() != null) {
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().getContent().add(new JAXBElement(new QName(URN_HL_7_RU_IDENTITY, PATRONYMIC), String.class, recipe.getPatient().getMiddleName()));
        }
        if (recipe.getPatient().getBirthDate() != null) {
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().setBirthTime(new TS());
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getBirthTime().setValue(recipe.getPatient().getBirthDate().format(DateTimeFormatter.ofPattern(IEMK_DATE_FORMAT)));
        }
        if (recipe.getPatient().getGender() != null) {
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().setAdministrativeGenderCode(new POCDMT000040Patient.AdministrativeGenderCode());
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().setCode(recipe.getPatient().getGender().equals("М") ? "1" : "2");
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().setCodeSystem("1.2.643.5.1.13.13.11.1040");
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().setCodeSystemName("Пол пациента");
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().setCodeSystemVersion("2.1");
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().setDisplayName(recipe.getPatient().getGender().equals("М") ? "Мужской" : "Женский");
        }

        // Медицинская организация, оформившая рецепт
        clinicalDocument.getRecordTarget().getPatientRole().setProviderOrganization(new POCDMT000040OrganizationPatient());
        var id = new POCDMT000040OrganizationPatient.Id();
        id.setRoot(recipe.getProviderOrganization().getOid());
        id.setExtension(recipe.getProviderOrganization().getOid() + ".0.18881");
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getId().add(id);

        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().setProps(new POCDMT000040Props());
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getProps().setOgrn(recipe.getProviderOrganization().getOgrn());
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getProps().setOgrnip(new ST());
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getProps().getOgrnip().getNullFlavor().add("NA");
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().setName(recipe.getProviderOrganization().getName());

        if (recipe.getProviderOrganization().getPhone() != null) {
            var tel = new TEL();
            tel.getUse().add("WP");
            tel.setValue("tel:" + recipe.getProviderOrganization().getPhone());
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getTelecom().add(tel);
        }

        if (recipe.getProviderOrganization().getAddress() != null) {
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().setAddr(new POCDMT000040OrganizationPatient.Addr());
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().setStateCode(new CDAddressStateCode());
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getStateCode().setCode(recipe.getProviderOrganization().getAddress().getRegionCode());
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getStateCode().setCodeSystem(STATE_CODE_SYSTEM);
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getStateCode().setCodeSystemVersion("6.3");
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getStateCode().setCodeSystemName(STATE_CODE_SYSTEM_NAME);
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getStateCode().setDisplayName(recipe.getProviderOrganization().getAddress().getRegionName());
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().setStreetAddressLine(recipe.getProviderOrganization().getAddress().getFullAddress());
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().setPostalCode(new AdxpPostalCode());
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getPostalCode().getNullFlavor().add("NA");

            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().setAddress(new FiasAddress());
            if (recipe.getProviderOrganization().getAddress().getAoGuid() != null && recipe.getProviderOrganization().getAddress().getHouseGuid() != null) {
                clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getAddress().setAOGUID(recipe.getProviderOrganization().getAddress().getAoGuid());
                clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getAddress().setHOUSEGUID(recipe.getProviderOrganization().getAddress().getHouseGuid());
            } else {
                clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getAddress().getNullFlavor().add("NI");
            }
        }

        // Данные об авторе документа
        clinicalDocument.setAuthor(new POCDMT000040Author());
        clinicalDocument.getAuthor().setTime(new POCDMT000040Time());
        clinicalDocument.getAuthor().getTime().getNullFlavor().add("NI");
        clinicalDocument.getAuthor().setAssignedAuthor(new POCDMT000040AssignedAuthor());

        // Уникальный идентификатор автора в МИС
        var authorId = new IIId();
        authorId.setRoot(recipe.getAuthor().getRepresentedOrganisation().getOid() + OID_ROOT);
        authorId.setExtension(recipe.getAuthor().getMisId());
        clinicalDocument.getAuthor().getAssignedAuthor().getId().add(authorId);

        // Телефон автора
        var authorPhone = new TEL();
        if (recipe.getAuthor().getPhone() != null) {
            authorPhone.setValue(recipe.getAuthor().getPhone());
        } else {
            authorPhone.setValue("NA");
        }
        clinicalDocument.getAuthor().getAssignedAuthor().getTelecom().add(authorPhone);

        // СНИЛС автора
        var authorSnils = new IIId();
        authorSnils.setRoot(SNILS_ROOT);
        authorSnils.setExtension(recipe.getAuthor().getSnils());
        clinicalDocument.getAuthor().getAssignedAuthor().getId().add(authorSnils);

        // Код должности автора
        clinicalDocument.getAuthor().getAssignedAuthor().setCode(new POCDMT000040CodeProfession());
        clinicalDocument.getAuthor().getAssignedAuthor().getCode().setCode(recipe.getAuthor().getPost().getCode().toString());
        clinicalDocument.getAuthor().getAssignedAuthor().getCode().setCodeSystem(POST_CODE_SYSTEM);
        clinicalDocument.getAuthor().getAssignedAuthor().getCode().setCodeSystemName(POST_CODE_SYSTEM_NAME);
        clinicalDocument.getAuthor().getAssignedAuthor().getCode().setCodeSystemVersion("7.5");
        clinicalDocument.getAuthor().getAssignedAuthor().getCode().setDisplayName(recipe.getAuthor().getPost().getName());

        // ФИО
        clinicalDocument.getAuthor().getAssignedAuthor().setAssignedPerson(new POCDMT000040Person());
        clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().setName(new PN());
        clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, FAMILY), String.class, recipe.getAuthor().getLastName()));
        clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, GIVEN), String.class, recipe.getAuthor().getFirstName()));
        if (recipe.getAuthor().getMiddleName() != null) {
            clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().getContent().add(new JAXBElement(new QName(URN_HL_7_RU_IDENTITY, PATRONYMIC), String.class, recipe.getAuthor().getMiddleName()));
        }

        // Место работы автора
        clinicalDocument.getAuthor().getAssignedAuthor().setRepresentedOrganization(new POCDMT000040AssignedAuthor.RepresentedOrganization());
        clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().setClassCode("ORG");
        clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().setId(new IIIdOrgNonNF());
        clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getId().setRoot(recipe.getAuthor().getRepresentedOrganisation().getOid());
        clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().setName(recipe.getAuthor().getRepresentedOrganisation().getName());

        if (recipe.getAuthor().getRepresentedOrganisation().getAddress() != null) {
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().setAddr(new POCDMT000040AssignedEntityLegalAuthenticator.RepresentedOrganization.Addr());
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().setStateCode(new CDAddressStateCode());
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getStateCode().setCode(recipe.getAuthor().getRepresentedOrganisation().getAddress().getRegionCode());
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getStateCode().setCodeSystem(STATE_CODE_SYSTEM);
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getStateCode().setCodeSystemVersion("6.3");
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getStateCode().setCodeSystemName(STATE_CODE_SYSTEM_NAME);
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getStateCode().setDisplayName(recipe.getAuthor().getRepresentedOrganisation().getAddress().getRegionName());
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().setStreetAddressLine(recipe.getAuthor().getRepresentedOrganisation().getAddress().getFullAddress());
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().setPostalCode(new AdxpPostalCode());
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getPostalCode().getNullFlavor().add("NA");
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().setAddress(new FiasAddress());
            if (recipe.getAuthor().getRepresentedOrganisation().getAddress().getAoGuid() != null && recipe.getAuthor().getRepresentedOrganisation().getAddress().getHouseGuid() != null) {
                clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getAddress().setAOGUID(recipe.getAuthor().getRepresentedOrganisation().getAddress().getAoGuid());
                clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getAddress().setHOUSEGUID(recipe.getAuthor().getRepresentedOrganisation().getAddress().getHouseGuid());
            } else {
                clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getAddress().getNullFlavor().add("NI");
            }
        }

        // Данные об организации владельце документа
        clinicalDocument.setCustodian(new POCDMT000040Custodian());
        clinicalDocument.getCustodian().setAssignedCustodian(new POCDMT000040AssignedCustodian());
        clinicalDocument.getCustodian().getAssignedCustodian().setRepresentedCustodianOrganization(new POCDMT000040CustodianOrganization());
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().setClassCode("ORG");
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().setId(new POCDMT000040CustodianOrganization.Id());
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getId().setRoot(recipe.getCustodianOrganisation().getOid());
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getId().setExtension(recipe.getCustodianOrganisation().getOid() + ".0.18881");
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().setName(recipe.getCustodianOrganisation().getName());

        if (recipe.getCustodianOrganisation().getAddress() != null) {
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().setAddr(new POCDMT000040CustodianOrganization.Addr());
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().setStateCode(new CDAddressStateCode());
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getStateCode().setCode(recipe.getCustodianOrganisation().getAddress().getRegionCode());
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getStateCode().setCodeSystem(STATE_CODE_SYSTEM);
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getStateCode().setCodeSystemVersion("6.3");
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getStateCode().setCodeSystemName(STATE_CODE_SYSTEM_NAME);
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getStateCode().setDisplayName(recipe.getCustodianOrganisation().getAddress().getRegionName());
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().setStreetAddressLine(recipe.getCustodianOrganisation().getAddress().getFullAddress());
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().setPostalCode(new AdxpPostalCode());
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getPostalCode().getNullFlavor().add("NA");
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().setAddress(new FiasAddress());
            if (recipe.getCustodianOrganisation().getAddress().getAoGuid() != null && recipe.getCustodianOrganisation().getAddress().getHouseGuid() != null) {
                clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getAddress().setAOGUID(recipe.getCustodianOrganisation().getAddress().getAoGuid());
                clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getAddress().setHOUSEGUID(recipe.getCustodianOrganisation().getAddress().getHouseGuid());
            } else {
                clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getAddress().getNullFlavor().add("NI");
            }
        }

        // Данные о получателе документа МЗ РФ
        var informationRecipient = new POCDMT000040InformationRecipient();
        informationRecipient.setIntendedRecipient(new POCDMT000040IntendedRecipient());
        informationRecipient.getIntendedRecipient().setReceivedOrganization(new POCDMT000040OrganizationInformationRecipient());
        informationRecipient.getIntendedRecipient().getReceivedOrganization().setId(new IIIdOrg());
        informationRecipient.getIntendedRecipient().getReceivedOrganization().getId().setRoot("1.2.643.5.1.13");
        informationRecipient.getIntendedRecipient().getReceivedOrganization().setName("Министерство здравоохранения Российской Федерации");
        clinicalDocument.getInformationRecipient().add(informationRecipient);

        // Данные о лице придавшем юридическую силу документу
        clinicalDocument.setLegalAuthenticator(new POCDMT000040LegalAuthenticator());

        // Дата подписи документа лицом, придавшем юридическую силу документу
        clinicalDocument.getLegalAuthenticator().setTime(new POCDMT000040Time());
        clinicalDocument.getLegalAuthenticator().getTime().getNullFlavor().add("NI");
        clinicalDocument.getLegalAuthenticator().setSignatureCode(new POCDMT000040LegalAuthenticator.SignatureCode());

        // Факт наличия подписи на документе
        clinicalDocument.getLegalAuthenticator().getSignatureCode().getNullFlavor().add("NI");

        // Лицо, придавшее юридическую силу документу (роль)
        clinicalDocument.getLegalAuthenticator().setAssignedEntity(new POCDMT000040AssignedEntityLegalAuthenticator());

        // Уникальный идентификатор лица придавшего юридическую силу документу в МИС
        var legalAuthenticatorId = new IIId();
        legalAuthenticatorId.setRoot(recipe.getLegalAuthenticator().getRepresentedOrganisation().getOid() + OID_ROOT);
        legalAuthenticatorId.setExtension(recipe.getLegalAuthenticator().getMisId());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getId().add(legalAuthenticatorId);

        // СНИЛС лица придавшего юридическую силу документу
        var legalAuthenticatorSnils = new IIId();
        legalAuthenticatorSnils.setRoot(SNILS_ROOT);
        legalAuthenticatorSnils.setExtension(recipe.getLegalAuthenticator().getSnils());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getId().add(legalAuthenticatorSnils);

        // Код должности лица, придавшего юридическую силу документу
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().setCode(new POCDMT000040CodeProfession());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().setCode(recipe.getLegalAuthenticator().getPost().getCode().toString());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().setCodeSystem(POST_CODE_SYSTEM);
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().setCodeSystemName(POST_CODE_SYSTEM_NAME);
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().setCodeSystemVersion("7.5");
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().setDisplayName(recipe.getLegalAuthenticator().getPost().getName());

        // ФИО
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().setAssignedPerson(new POCDMT000040Person());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().setName(new PN());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, FAMILY), String.class, recipe.getLegalAuthenticator().getLastName()));
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, GIVEN), String.class, recipe.getLegalAuthenticator().getFirstName()));
        if (recipe.getLegalAuthenticator().getMiddleName() != null) {
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().getContent().add(new JAXBElement(new QName(URN_HL_7_RU_IDENTITY, PATRONYMIC), String.class, recipe.getLegalAuthenticator().getMiddleName()));
        }

        // Место работы лица, придавшего юридическую силу документу
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().setRepresentedOrganization(new POCDMT000040AssignedEntityLegalAuthenticator.RepresentedOrganization());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().setClassCode("ORG");
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().setId(new IIIdOrgNonNF());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getId().setRoot(recipe.getLegalAuthenticator().getRepresentedOrganisation().getOid());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().setName(recipe.getLegalAuthenticator().getRepresentedOrganisation().getName());
        if (recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress() != null) {
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().setAddr(new POCDMT000040AssignedEntityLegalAuthenticator.RepresentedOrganization.Addr());
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().setStateCode(new CDAddressStateCode());
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getStateCode().setCode(recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getRegionCode());
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getStateCode().setCodeSystem(STATE_CODE_SYSTEM);
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getStateCode().setCodeSystemVersion("6.3");
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getStateCode().setCodeSystemName(STATE_CODE_SYSTEM_NAME);
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getStateCode().setDisplayName(recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getRegionName());
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().setStreetAddressLine(recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getFullAddress());
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().setPostalCode(new AdxpPostalCode());
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getPostalCode().getNullFlavor().add("NA");
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().setAddress(new FiasAddress());
            if (recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getAoGuid() != null && recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getHouseGuid() != null) {
                clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getAddress().setAOGUID(recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getAoGuid());
                clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getAddress().setHOUSEGUID(recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getHouseGuid());
            } else {
                clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getAddress().getNullFlavor().add("NI");
            }
        }

        // Сведения о случае оказания мед. помощи
        if (preferential) {
            clinicalDocument.setComponentOf(new POCDMT000040Component1());
            clinicalDocument.getComponentOf().setEncompassingEncounter(new POCDMT000040EncompassingEncounter());
            clinicalDocument.getComponentOf().getEncompassingEncounter().setEffectiveTime(new POCDMT000040EncompassingEncounter.EffectiveTime());

            if (recipe.getMedicalCareCase() != null) {
                // Уникальный идентификатор случая оказания медицинской помощи
                var medicalCaseId = new POCDMT000040EncompassingEncounter.Id();
                medicalCaseId.setRoot(recipe.getProviderOrganization().getOid() + ".100.1.1.15");
                medicalCaseId.setExtension(recipe.getMedicalCareCase().getMisId().toString());
                clinicalDocument.getComponentOf().getEncompassingEncounter().getId().add(medicalCaseId);

                // Идентификатор случая оказания медицинской помощи (номер медицинской карты)
                var medicalCardNumber = new POCDMT000040EncompassingEncounter.Id();
                medicalCardNumber.setRoot(recipe.getProviderOrganization().getOid() + ".100.1.1.16");
                medicalCardNumber.setExtension(recipe.getMedicalCareCase().getMedicalCardNumber());
                clinicalDocument.getComponentOf().getEncompassingEncounter().getId().add(medicalCardNumber);

                // Даты начала и окончания случая
                clinicalDocument.getComponentOf().getEncompassingEncounter().getEffectiveTime().setLow(new POCDMT000040EncompassingEncounter.EffectiveTime.Low());
                clinicalDocument.getComponentOf().getEncompassingEncounter().getEffectiveTime().getLow().setValue(recipe.getMedicalCareCase().getBeginDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
                clinicalDocument.getComponentOf().getEncompassingEncounter().getEffectiveTime().setHigh(new IVXBTS());
                clinicalDocument.getComponentOf().getEncompassingEncounter().getEffectiveTime().getHigh().setValue(recipe.getMedicalCareCase().getEndDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
            }
        }

        // Тело документа
        clinicalDocument.setComponent(new POCDMT000040Component2());
        clinicalDocument.getComponent().setStructuredBody(new POCDMT000040StructuredBody());

        // Секция сведения о документе
        var documentSection = new POCDMT000040Component3();
        documentSection.setSection(new POCDMT000040Section());

        // Код секции
        documentSection.getSection().setCode(new POCDMT000040Section.Code());
        documentSection.getSection().getCode().setCode(DOC_INFO_CODE);
        documentSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
        documentSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
        documentSection.getSection().getCode().setCodeSystemVersion(version == 3 ? "1.19" : "2.1");
        documentSection.getSection().getCode().setDisplayName(DOC_INFO_NAME);

        if (preferential) {
            documentSection.getSection().setTitle(EL_RECIPE_TITLE);
        } else {
            documentSection.getSection().setTitle(DOC_TITLE);
        }

        // Наполнение секции
        documentSection.getSection().setText(new StrucDocText());
        var strucDocTable = new StrucDocTable();
        var strucDocTbody = new StrucDocTbody();

        if (preferential) {
            // Приоритет исполнения
            if (recipe.getPriority() != null) {
                strucDocTbody.getTr().add(
                        createTr(RECIPE_PRIORITY, recipe.getPriority())
                );
                documentSection.getSection().getEntry().add(
                        createEntry(
                                "6000",
                                RECIPE_PRIORITY,
                                recipe.getPriority().equalsIgnoreCase("срочно") ? "1" : "2",
                                "1.2.643.5.1.13.13.99.2.609",
                                RECIPE_PRIORITY,
                                "1.1",
                                recipe.getPriority(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                "CD",
                                version
                        )
                );
            }

            // Серия рецепта
            strucDocTbody.getTr().add(
                    createTr(RECIPE_SERIES_NAME, recipe.getSeries())
            );
            documentSection.getSection().getEntry().add(
                    createEntry(
                            "6001",
                            RECIPE_SERIES_NAME,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            recipe.getSeries(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            "ST",
                            version
                    )
            );

            // Номер рецепта
            strucDocTbody.getTr().add(
                    createTr(RECIPE_NUMBER_NAME, recipe.getNumber())
            );
            documentSection.getSection().getEntry().add(
                    createEntry(
                            "6002",
                            RECIPE_NUMBER_NAME,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            recipe.getNumber(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            "ST",
                            version
                    )
            );

            // Протокол врачебной комиссии
            if (recipe.getMedicalCommissionProtocol() != null) {
                strucDocTbody.getTr().add(
                        createTr(
                                PROTOCOL_NAME,
                                recipe.getMedicalCommissionProtocol().getNumber() + " от " + recipe.getMedicalCommissionProtocol().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                        )
                );
                documentSection.getSection().getEntry().add(
                        createEntry(
                                "4059",
                                PROTOCOL_NAME,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                recipe.getMedicalCommissionProtocol().getNumber(),
                                recipe.getMedicalCommissionProtocol().getDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)),
                                null,
                                null,
                                null,
                                null,
                                "ST",
                                version
                        )
                );
            }
        }

        // Срок действия рецепта
        strucDocTbody.getTr().add(
                createTr(RECIPE_VALIDITY, recipe.getValidity())
        );
        documentSection.getSection().getEntry().add(
                createEntry(
                        "6004",
                        RECIPE_VALIDITY,
                        Validity.getValidityByName(recipe.getValidity()).getCode(),
                        RECIPE_VALIDITY_CODE_SYSTEM,
                        RECIPE_VALIDITY,
                        "1.2",
                        recipe.getValidity(),
                        null,
                        null,
                        recipe.getEndDate().atStartOfDay(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)),
                        null,
                        null,
                        null,
                        null,
                        "CD",
                        version
                )
        );

        // Дата окончания действия рецепта
        if (recipe.getEndDate() != null) {
            strucDocTbody.getTr().add(
                    createTr(
                            END_DATE_DISPLAY_NAME,
                            recipe.getEndDate().format(DateTimeFormatter.ofPattern(RUS_DATE_FORMAT))
                    )
            );
        }

        // По специальному назначению (Отметка)
        strucDocTbody.getTr().add(
                createTr(SPECIAL_NAME, recipe.isSpecial() ? "имеется" : "отсутствует")
        );
        documentSection.getSection().getEntry().add(
                createEntry(
                        "6006",
                        SPECIAL_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        String.valueOf(recipe.isSpecial()),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        "BL",
                        version
                )
        );

        if (recipe.getMkb() != null) {
            // Код заболевания по МКБ-10
            strucDocTbody.getTr().add(
                    createTr("Код заболевания по МКБ-10", recipe.getMkb().getCode())
            );
            documentSection.getSection().getEntry().add(
                    createEntry(
                            "809",
                            "Шифр по МКБ-10",
                            recipe.getMkb().getCode(),
                            "1.2.643.5.1.13.13.11.1005",
                            "Международная статистическая классификация болезней и проблем, связанных со здоровьем (10-й пересмотр)",
                            "2.19",
                            recipe.getMkb().getName(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "CD",
                            version
                    )
            );
        }

        // Наличие хронических заболеваний
        if (preferential) {
            documentSection.getSection().getEntry().add(
                    createEntry(
                            "11001",
                            "Наличие хронических заболеваний",
                            null,
                            null,
                            null,
                            null,
                            null,
                            String.valueOf(recipe.isChronicDisease()),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "BL",
                            version
                    )
            );
        }

        strucDocTable.getTbody().add(strucDocTbody);
        documentSection.getSection().getText().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, TABLE), StrucDocTable.class, strucDocTable));
        clinicalDocument.getComponent().getStructuredBody().getComponent().add(documentSection);

        if (preferential && recipe.getBenefit() != null && recipe.getFinancing() != null) {
            // Секция коды льгот (с кодированными элементами)
            var benefitSection = new POCDMT000040Component3();
            benefitSection.setSection(new POCDMT000040Section());
            var strucDocTableBenefit = new StrucDocTable();
            var strucDocTbodyBenefit = new StrucDocTbody();

            // Код секции
            benefitSection.getSection().setCode(new POCDMT000040Section.Code());
            benefitSection.getSection().getCode().setCode("BENEFITS");
            benefitSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
            benefitSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
            benefitSection.getSection().getCode().setCodeSystemVersion("1.19");
            benefitSection.getSection().getCode().setDisplayName("Льготы");

            benefitSection.getSection().setTitle("Коды льгот");

            // Наполнение секции
            benefitSection.getSection().setText(new StrucDocText());

            // Льготная категория
            strucDocTbodyBenefit.getTr().add(
                    createTr(BENEFIT_NAME, recipe.getBenefit().getName())
            );
            benefitSection.getSection().getEntry().add(
                    createEntry(
                            "811",
                            BENEFIT_NAME,
                            recipe.getBenefit().getCode(),
                            BENEFIT_CODE_SYSTEM,
                            BENEFIT_CODE_SYSTEM_NAME,
                            "6.14",
                            recipe.getBenefit().getName(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "CD",
                            version
                    )
            );

            // Источник финансирования
            strucDocTbodyBenefit.getTr().add(
                    createTr(FINANCING_NAME, recipe.getFinancing().getName())
            );

            // Размер льготы код
            benefitSection.getSection().getEntry().add(
                    createEntry(
                            "6009",
                            "Размер льготы (код)",
                            "102",
                            "1.2.643.5.1.13.13.99.2.605",
                            "Виды предоставляемых льгот",
                            "3.2",
                            recipe.getBenefit().getAmount(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "CD",
                            version
                    )
            );

            // Размер льготы %
            strucDocTbodyBenefit.getTr().add(
                    createTr("Размер льготы", recipe.getBenefit().getPercent() + "%")
            );
            benefitSection.getSection().getEntry().add(
                    createEntry(
                            "6010",
                            "Размер льготы (значение в процентах)",
                            null,
                            null,
                            null,
                            null,
                            null,
                            recipe.getBenefit().getPercent(),
                            null,
                            null,
                            "%",
                            recipe.getBenefit().getPercent(),
                            "53",
                            "%",
                            "PQ",
                            version
                    )
            );

            strucDocTableBenefit.getTbody().add(strucDocTbodyBenefit);
            benefitSection.getSection().getText().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, TABLE), StrucDocTable.class, strucDocTableBenefit));
            clinicalDocument.getComponent().getStructuredBody().getComponent().add(benefitSection);
        }

        return clinicalDocument;
    }

    private StrucDocTr createTr(String name, String content) {
        var tr = new StrucDocTr();
        var tdTitle = new StrucDocTd();
        tdTitle.getContent().add(createStrucDocContent(name));
        tr.getThOrTd().add(tdTitle);
        var tdContent = new StrucDocTd();
        tdContent.getContent().add(createStrucDocContent(content));
        tr.getThOrTd().add(tdContent);
        return tr;
    }

    private JAXBElement createStrucDocContent(String content) {
        var strucDocContent = new StrucDocContent();
        strucDocContent.getContent().add(content);
        return new JAXBElement(new QName(URN_HL_7_ORG_V_3, "content"), StrucDocContent.class, strucDocContent);
    }

    private POCDMT000040Entry createEntry(
            String code,
            String codeDisplayName,
            String valueCode,
            String valueCodeSystem,
            String valueCodeSystemName,
            String valueCodeSystemVersion,
            String valueDisplayName,
            String value,
            String valueContent,
            String effectiveTime,
            String unit,
            String translationValue,
            String translationCode,
            String translationDisplayName,
            String valueType,
            int version
    ) {
        var entry = new POCDMT000040Entry();
        entry.setObservation(new POCDMT000040Observation());
        entry.getObservation().getClassCode().add("OBS");
        entry.getObservation().setMoodCode("EVN");
        entry.getObservation().setCode(new POCDMT000040Observation.Code());
        entry.getObservation().getCode().setCode(code);
        entry.getObservation().getCode().setCodeSystem(IEMK_CODE_SYSTEM);
        entry.getObservation().getCode().setCodeSystemName(IEMK_CODE_SYSTEM_NAME);
        entry.getObservation().getCode().setCodeSystemVersion(version == 3 ? "1.75" : "2.1");
        entry.getObservation().getCode().setDisplayName(codeDisplayName);
        if (valueType.equals("CD")) {
            var cd = new CD();
            cd.setCode(valueCode);
            cd.setCodeSystem(valueCodeSystem);
            cd.setCodeSystemName(valueCodeSystemName);
            cd.setCodeSystemVersion(valueCodeSystemVersion);
            cd.setDisplayName(valueDisplayName);
            entry.getObservation().setValue(cd);
        }
        if (valueType.equals("BL")) {
            var bl = new BL();
            bl.setValue(value);
            entry.getObservation().setValue(bl);
        }
        if (valueType.equals("ST")) {
            var st = new ST();
            st.getContent().add(valueContent);
            entry.getObservation().setValue(st);
        }
        if (valueType.equals("TS")) {
            var ts = new TS();
            ts.setValue(value);
            entry.getObservation().setValue(ts);
        }
        if (valueType.equals("PQ")) {
            var pq = new PQ();
            pq.setValue(value);
            pq.setUnit(unit);
            var pqr = new PQR();
            pqr.setCode(translationCode);
            pqr.setCodeSystem(TRANSLATION_CODE_SYSTEM);
            pqr.setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
            pqr.setCodeSystemVersion("3.2");
            pqr.setDisplayName(translationDisplayName);
            pqr.setValue(translationValue);
            pq.getTranslation().add(pqr);
            entry.getObservation().setValue(pq);
        }
        if (effectiveTime != null) {
            entry.getObservation().setEffectiveTime(new POCDMT000040Observation.EffectiveTime());
            entry.getObservation().getEffectiveTime().setValue(effectiveTime);
        }
        return entry;
    }
}
