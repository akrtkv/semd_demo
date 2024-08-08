package su.medsoft.rir.recipe.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import su.medsoft.rir.recipe.dto.iemk.ClinicalDocument;
import su.medsoft.rir.recipe.dto.iemk.fias.Address;
import su.medsoft.rir.recipe.dto.iemk.identity.IdentityDoc;
import su.medsoft.rir.recipe.dto.iemk.identity.IdentityDocElement;
import su.medsoft.rir.recipe.dto.iemk.identity.Ogrnip;
import su.medsoft.rir.recipe.dto.iemk_new_edition.*;
import su.medsoft.rir.recipe.dto.iemk_new_edition.address.Type;
import su.medsoft.rir.recipe.dto.iemk_new_edition.fias.FiasAddress;
import su.medsoft.rir.recipe.dto.iemk_new_edition.identity.POCDMT000040IdentityDoc;
import su.medsoft.rir.recipe.dto.iemk_new_edition.identity.POCDMT000040InsurancePolicy;
import su.medsoft.rir.recipe.dto.iemk_new_edition.identity.POCDMT000040Props;
import su.medsoft.rir.recipe.dto.rir.Insurance;
import su.medsoft.rir.recipe.dto.rir.InsuranceOrganisation;
import su.medsoft.rir.recipe.dto.rir.Patient;
import su.medsoft.rir.recipe.dto.rir.Validity;
import su.medsoft.rir.recipe.dto.rir.recipe.*;
import su.medsoft.rir.recipe.exception.DrugNotFoundException;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

import static su.medsoft.rir.recipe.utils.Constants.*;

@Component
public class IemkObjectCreator {

    private final Converter converter;

    private final MessageSource messageSource;

    @Autowired
    public IemkObjectCreator(Converter converter, MessageSource messageSource) {
        this.converter = converter;
        this.messageSource = messageSource;
    }

    // Льготный рецепт
    @Deprecated
    public String createIemkPreferentialRecipeDocument(FullRecipe fullRecipe) {
        if (fullRecipe.getDrug() != null) {
            return createIemkPreferentialDrugRecipeDocument(fullRecipe);
        } else if (fullRecipe.getMedicalProduct() != null) {
            return createIemkPreferentialMedicalProductRecipeDocument(fullRecipe);
        } else {
            return createIemkPreferentialHealthFoodRecipeDocument(fullRecipe);
        }
    }

    private String createIemkPreferentialDrugRecipeDocument(FullRecipe drugRecipe) {
        var clinicalDocument = createIemkClinicalDocumentCommonData("1.2.643.5.1.13.13.14.37.3", "37", PREFERENTIAL_RECIPE_TITLE, drugRecipe, true);
        createPreferentialRecipeCommonData(drugRecipe, clinicalDocument);

        // Секция назначение лекарственного препарата\специализированного продукта лечебного питания
        var recipeSection = new ClinicalDocument.Component.StructuredBody.ComponentSection();
        recipeSection.setSection(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section());

        // Код секции
        recipeSection.getSection().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Code());
        recipeSection.getSection().getCode().setCode(RECIPE);
        recipeSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
        recipeSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
        recipeSection.getSection().getCode().setCodeSystemVersion("1.7");
        recipeSection.getSection().getCode().setDisplayName(RECIPE_NAME);

        recipeSection.getSection().setTitle(PREFERENTIAL_RECIPE_SECTION_TITLE);

        // Наполнение секции
        recipeSection.getSection().setText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text());
        recipeSection.getSection().getText().setTable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table());
        recipeSection.getSection().getText().getTable().setTbody(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody());
        recipeSection.getSection().getText().getTable().setThead(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Thead());
        recipeSection.getSection().getText().getTable().getThead().setTr(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Thead.Tr());

        recipeSection.getSection().getText().getTable().getThead().getTr().getTh().add(APPOINTED);
        recipeSection.getSection().getText().getTable().getTbody().getTr().add(createRecipeSectionTr());
        recipeSection.getSection().getText().getTable().getTbody().getTr().get(0).getTd().add(createRecipeSectionTd("med-1", drugRecipe.getDrug().getName() + (drugRecipe.getDrug().getTradeName() != null ? " (торговое наименование " + drugRecipe.getDrug().getTradeName() + ")" : "")));
        if (drugRecipe.getDrug().getDose() != null) {
            recipeSection.getSection().getText().getTable().getThead().getTr().getTh().add(DOSING_NAME);
            recipeSection.getSection().getText().getTable().getTbody().getTr().get(0).getTd().add(createRecipeSectionTd("med-2", drugRecipe.getDrug().getDose().toString()));
        }

        var recipeSectionEntry = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry();
        recipeSectionEntry.setSubstanceAdministration(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration());
        recipeSectionEntry.getSubstanceAdministration().setClassCode(SBADM);
        recipeSectionEntry.getSubstanceAdministration().setMoodCode("RQO");

        if (drugRecipe.getBenefit() != null) {
            // Кодирование типов назначений льготного рецепта
            recipeSectionEntry.getSubstanceAdministration().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Code());
            recipeSectionEntry.getSubstanceAdministration().getCode().setCode(drugRecipe.getBenefit().getTypeCode().shortValue());
            recipeSectionEntry.getSubstanceAdministration().getCode().setCodeSystem(RECIPE_TYPE_CODE_SYSTEM);
            recipeSectionEntry.getSubstanceAdministration().getCode().setCodeSystemName(RECIPE_TYPE_CODE_SYSTEM_NAME);
            recipeSectionEntry.getSubstanceAdministration().getCode().setCodeSystemVersion("1.2");
            recipeSectionEntry.getSubstanceAdministration().getCode().setDisplayName(drugRecipe.getBenefit().getTypeName());
        }

        // Наполнение секции
        recipeSectionEntry.getSubstanceAdministration().setText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Text());
        recipeSectionEntry.getSubstanceAdministration().getText().setReference(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Text.Reference());
        recipeSectionEntry.getSubstanceAdministration().getText().getReference().setValue("#med");

        // Длительность приема препарата
        recipeSectionEntry.getSubstanceAdministration().setEffectiveTime(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EffectiveTime());
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().setType(IVL_TS);
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().setWidth(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EffectiveTime.Width());
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().setUnit("d");
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().setValue(drugRecipe.getDrug().getDurationDays());
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().setTranslation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EffectiveTime.Width.Translation());
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCode((short) 24);
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCodeSystem(TRANSLATION_CODE_SYSTEM);
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation()
                .setCodeSystemVersion("2.6");
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setDisplayName("сут");

        // Путь введения препарата

        if (drugRecipe.getDrug().getRouteCode() != null && drugRecipe.getDrug().getRouteName() != null) {
            recipeSectionEntry.getSubstanceAdministration().setRouteCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.RouteCode());
            recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCode(drugRecipe.getDrug().getRouteCode().shortValue());
            recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystem(ROUTE_CODE_SYSTEM);
            recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemName(ROUTE_CODE_SYSTEM_NAME);
            recipeSectionEntry.getSubstanceAdministration().getRouteCode()
                    .setCodeSystemVersion("1.2");
            recipeSectionEntry.getSubstanceAdministration().getRouteCode().setDisplayName(drugRecipe.getDrug().getRouteName());
        }

        // Назначенный препарат\специализированный продукт лечебного питания
        recipeSectionEntry.getSubstanceAdministration().setConsumable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().setTypeCode("CSM");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().setManufacturedProduct(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable.ManufacturedProduct());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setClassCode("MANU");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setManufacturedMaterial(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable.ManufacturedProduct.ManufacturedMaterial());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setClassCode("MMAT");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setDeterminerCode("KIND");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable.ManufacturedProduct.ManufacturedMaterial.Code());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCode(drugRecipe.getDrug().getCodeMnn());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystem(MATERIAL_CODE_SYSTEM);
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemName(MATERIAL_CODE_SYSTEM_NAME);
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemVersion("5.36");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setDisplayName(drugRecipe.getDrug().getName());
        if (drugRecipe.getDrug().getTradeName() != null) {
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setName(drugRecipe.getDrug().getTradeName());
        }

        // Количество назначенных доз
        if (drugRecipe.getDrug().getDose() != null) {
            var doseEntry = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship();
            doseEntry.setTypeCode("COMP");
            doseEntry.setObservation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation());
            doseEntry.getObservation().setClassCode("OBS");
            doseEntry.getObservation().setMoodCode("EVN");
            doseEntry.getObservation().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Code());
            doseEntry.getObservation().getCode().setCode(6011);
            doseEntry.getObservation().getCode().setCodeSystem(OBSERVATION_CODE_SYSTEM);
            doseEntry.getObservation().getCode().setCodeSystemName(OBSERVATION_CODE_SYSTEM_NAME);
            doseEntry.getObservation().getCode().setCodeSystemVersion("1.31");
            doseEntry.getObservation().getCode().setDisplayName(DOSING_NAME);
            doseEntry.getObservation().getCode().setOriginalText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Code.OriginalText());
            doseEntry.getObservation().getCode().getOriginalText().setReference(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Code.OriginalText.Reference());
            doseEntry.getObservation().getCode().getOriginalText().getReference().setValue(DOC_1);
            doseEntry.getObservation().setValue(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Value());
            doseEntry.getObservation().getValue().setValue(drugRecipe.getDrug().getDose().shortValue());
            doseEntry.getObservation().getValue().setType("PQ");
            doseEntry.getObservation().getValue().setUnit("U");
            doseEntry.getObservation().getValue().setTranslation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Value.Translation());
            doseEntry.getObservation().getValue().getTranslation().setCode((short) 128);
            doseEntry.getObservation().getValue().getTranslation().setCodeSystem(TRANSLATION_CODE_SYSTEM);
            doseEntry.getObservation().getValue().getTranslation().setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
            doseEntry.getObservation().getValue().getTranslation().setCodeSystemVersion("2.6");
            doseEntry.getObservation().getValue().getTranslation().setDisplayName(VALUE);
            doseEntry.getObservation().getValue().getTranslation().setValue(drugRecipe.getDrug().getDose().shortValue());
            recipeSectionEntry.getSubstanceAdministration().getEntryRelationship().add(doseEntry);
        }

        recipeSection.getSection().getEntry().add(recipeSectionEntry);

        clinicalDocument.getComponent().getStructuredBody().getComponent().add(recipeSection);

        return converter.convertObjectToXmlString(clinicalDocument, ClinicalDocument.class, true);
    }

    private String createIemkPreferentialMedicalProductRecipeDocument(FullRecipe medicalProductRecipe) {
        var clinicalDocument = createIemkClinicalDocumentCommonData("1.2.643.5.1.13.13.14.37.3", "37", PREFERENTIAL_RECIPE_TITLE, medicalProductRecipe, true);
        createPreferentialRecipeCommonData(medicalProductRecipe, clinicalDocument);

        // Секция рецепт на медицинское изделие
        var medProdSection = new ClinicalDocument.Component.StructuredBody.ComponentSection();
        medProdSection.setSection(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section());

        // Код секции
        medProdSection.getSection().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Code());
        medProdSection.getSection().getCode().setCode(RECIPE);
        medProdSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
        medProdSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
        medProdSection.getSection().getCode().setCodeSystemVersion("1.7");
        medProdSection.getSection().getCode().setDisplayName(RECIPE_NAME);

        medProdSection.getSection().setTitle("Назначение медицинского изделия");

        // Наполнение секции
        medProdSection.getSection().setText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text());
        medProdSection.getSection().getText().setTable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table());
        medProdSection.getSection().getText().getTable().setTbody(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody());
        medProdSection.getSection().getText().getTable().getTbody().getTr().add(
                createMedProdTr("Наименование медицинского изделия", medicalProductRecipe.getMedicalProduct().getName())
        );
        medProdSection.getSection().getText().getTable().getTbody().getTr().add(
                createMedProdTr("Количество", medicalProductRecipe.getMedicalProduct().getAmount().toString())
        );

        var medProdEntry = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry();
        medProdEntry.setSupply(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Supply());
        medProdEntry.getSupply().setClassCode("SPLY");
        medProdEntry.getSupply().setMoodCode("RQO");

        if (medicalProductRecipe.getBenefit() != null) {
            // Кодирование типа назначения льготного рецепта
            medProdEntry.getSupply().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Supply.Code());
            medProdEntry.getSupply().getCode().setCode(medicalProductRecipe.getBenefit().getTypeCode().shortValue());
            medProdEntry.getSupply().getCode().setCodeSystem(RECIPE_TYPE_CODE_SYSTEM);
            medProdEntry.getSupply().getCode().setCodeSystemName(RECIPE_TYPE_CODE_SYSTEM_NAME);
            medProdEntry.getSupply().getCode().setCodeSystemVersion("1.2");
            medProdEntry.getSupply().getCode().setDisplayName(medicalProductRecipe.getBenefit().getTypeName());
        }

        // Текст рецепта
        medProdEntry.getSupply().setText(medicalProductRecipe.getMedicalProduct().getName());

        medProdEntry.getSupply().setQuantity(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Supply.Quantity());
        medProdEntry.getSupply().getQuantity().setValue(medicalProductRecipe.getMedicalProduct().getAmount().shortValue());
        medProdEntry.getSupply().getQuantity().setTranslation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Supply.Quantity.Translation());
        medProdEntry.getSupply().getQuantity().getTranslation().setCode((short) 128);
        medProdEntry.getSupply().getQuantity().getTranslation().setCodeSystem("1.2.643.5.1.13.13.11.1358");
        medProdEntry.getSupply().getQuantity().getTranslation().setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
        medProdEntry.getSupply().getQuantity().getTranslation().setCodeSystemVersion("2.6");
        medProdEntry.getSupply().getQuantity().getTranslation().setDisplayName(VALUE);
        medProdEntry.getSupply().getQuantity().getTranslation().setValue(medicalProductRecipe.getMedicalProduct().getAmount().shortValue());

        medProdEntry.getSupply().setProduct(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Supply.Product());
        medProdEntry.getSupply().getProduct().setTypeCode("PRD");
        medProdEntry.getSupply().getProduct().setManufacturedProduct(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Supply.Product.ManufacturedProduct());
        medProdEntry.getSupply().getProduct().getManufacturedProduct().setClassCode("MANU");
        medProdEntry.getSupply().getProduct().getManufacturedProduct().setManufacturedMaterial(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Supply.Product.ManufacturedProduct.ManufacturedMaterial());
        medProdEntry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().setClassCode("MMAT");
        medProdEntry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().setDeterminerCode("KIND");
        medProdEntry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Supply.Product.ManufacturedProduct.ManufacturedMaterial.Code());
        medProdEntry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().setCode(medicalProductRecipe.getMedicalProduct().getCode());
        medProdEntry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystem("1.2.643.5.1.13.13.99.2.604");
        medProdEntry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemName("ФРЛЛО. Справочник медицинских изделий по классификации Казначейства России");
        medProdEntry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode()
                .setCodeSystemVersion("1.46");
        medProdEntry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().setDisplayName(medicalProductRecipe.getMedicalProduct().getName());
        medProdEntry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().setName(medicalProductRecipe.getMedicalProduct().getName());

        medProdSection.getSection().getEntry().add(medProdEntry);

        clinicalDocument.getComponent().getStructuredBody().getComponent().add(medProdSection);

        return converter.convertObjectToXmlString(clinicalDocument, ClinicalDocument.class, true);
    }

    private String createIemkPreferentialHealthFoodRecipeDocument(FullRecipe healthFoodRecipe) {
        var clinicalDocument = createIemkClinicalDocumentCommonData("1.2.643.5.1.13.13.14.37.3", "37", PREFERENTIAL_RECIPE_TITLE, healthFoodRecipe, true);
        createPreferentialRecipeCommonData(healthFoodRecipe, clinicalDocument);

        // Секция назначение лекарственного препарата\специализированного продукта лечебного питания
        var recipeSection = new ClinicalDocument.Component.StructuredBody.ComponentSection();
        recipeSection.setSection(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section());

        // Код секции
        recipeSection.getSection().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Code());
        recipeSection.getSection().getCode().setCode(RECIPE);
        recipeSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
        recipeSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
        recipeSection.getSection().getCode().setCodeSystemVersion("1.7");
        recipeSection.getSection().getCode().setDisplayName(RECIPE_NAME);

        recipeSection.getSection().setTitle(PREFERENTIAL_RECIPE_SECTION_TITLE);

        // Наполнение секции
        recipeSection.getSection().setText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text());
        recipeSection.getSection().getText().setTable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table());
        recipeSection.getSection().getText().getTable().setTbody(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody());
        recipeSection.getSection().getText().getTable().setThead(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Thead());
        recipeSection.getSection().getText().getTable().getThead().setTr(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Thead.Tr());

        recipeSection.getSection().getText().getTable().getThead().getTr().getTh().add(APPOINTED);
        recipeSection.getSection().getText().getTable().getTbody().getTr().add(createRecipeSectionTr());
        recipeSection.getSection().getText().getTable().getTbody().getTr().get(0).getTd().add(createRecipeSectionTd("med-1", healthFoodRecipe.getHealthFood().getName()));
        recipeSection.getSection().getText().getTable().getThead().getTr().getTh().add(DOSING_NAME);
        recipeSection.getSection().getText().getTable().getTbody().getTr().get(0).getTd().add(createRecipeSectionTd("med-2", healthFoodRecipe.getHealthFood().getDose().toString()));

        var recipeSectionEntry = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry();
        recipeSectionEntry.setSubstanceAdministration(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration());
        recipeSectionEntry.getSubstanceAdministration().setClassCode(SBADM);
        recipeSectionEntry.getSubstanceAdministration().setMoodCode("RQO");

        if (healthFoodRecipe.getBenefit() != null) {
            // Кодирование типов назначений льготного рецепта
            recipeSectionEntry.getSubstanceAdministration().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Code());
            recipeSectionEntry.getSubstanceAdministration().getCode().setCode(healthFoodRecipe.getBenefit().getTypeCode().shortValue());
            recipeSectionEntry.getSubstanceAdministration().getCode().setCodeSystem(RECIPE_TYPE_CODE_SYSTEM);
            recipeSectionEntry.getSubstanceAdministration().getCode().setCodeSystemName(RECIPE_TYPE_CODE_SYSTEM_NAME);
            recipeSectionEntry.getSubstanceAdministration().getCode().setCodeSystemVersion("1.2");
            recipeSectionEntry.getSubstanceAdministration().getCode().setDisplayName(healthFoodRecipe.getBenefit().getTypeName());
        }

        // Наполнение секции
        recipeSectionEntry.getSubstanceAdministration().setText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Text());
        recipeSectionEntry.getSubstanceAdministration().getText().setReference(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Text.Reference());
        recipeSectionEntry.getSubstanceAdministration().getText().getReference().setValue("#med");

        // Длительность приема препарата
        recipeSectionEntry.getSubstanceAdministration().setEffectiveTime(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EffectiveTime());
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().setType(IVL_TS);
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().setWidth(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EffectiveTime.Width());
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().setUnit("d");
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().setValue(healthFoodRecipe.getHealthFood().getDurationDays());
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().setTranslation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EffectiveTime.Width.Translation());
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCode(healthFoodRecipe.getHealthFood().getDurationDays().shortValue());
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCode((short) 24);
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCodeSystem(TRANSLATION_CODE_SYSTEM);
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCodeSystemVersion("2.6");
        recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setDisplayName("сут");

        // Путь введения препарата
        recipeSectionEntry.getSubstanceAdministration().setRouteCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.RouteCode());
        recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCode(healthFoodRecipe.getHealthFood().getRouteCode().shortValue());
        recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystem(ROUTE_CODE_SYSTEM);
        recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemName(ROUTE_CODE_SYSTEM_NAME);
        recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemVersion("1.2");
        recipeSectionEntry.getSubstanceAdministration().getRouteCode().setDisplayName(healthFoodRecipe.getHealthFood().getRouteName());

        // Назначенный препарат\специализированный продукт лечебного питания
        recipeSectionEntry.getSubstanceAdministration().setConsumable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().setTypeCode("CSM");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().setManufacturedProduct(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable.ManufacturedProduct());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setClassCode("MANU");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setManufacturedMaterial(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable.ManufacturedProduct.ManufacturedMaterial());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setClassCode("MMAT");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setDeterminerCode("KIND");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable.ManufacturedProduct.ManufacturedMaterial.Code());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCode(healthFoodRecipe.getHealthFood().getCode());
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystem("1.2.643.5.1.13.13.99.2.603");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemName("ФРЛЛО. Справочник специализированного питания по классификации Казначейства России");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode()
                .setCodeSystemVersion("1.1");
        recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setDisplayName(healthFoodRecipe.getHealthFood().getName());

        // Количество назначенных доз
        var doseEntry = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship();
        doseEntry.setTypeCode("COMP");
        doseEntry.setObservation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation());
        doseEntry.getObservation().setClassCode("OBS");
        doseEntry.getObservation().setMoodCode("EVN");
        doseEntry.getObservation().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Code());
        doseEntry.getObservation().getCode().setCode(6011);
        doseEntry.getObservation().getCode().setCodeSystem(OBSERVATION_CODE_SYSTEM);
        doseEntry.getObservation().getCode().setCodeSystemName(OBSERVATION_CODE_SYSTEM_NAME);
        doseEntry.getObservation().getCode().setCodeSystemVersion("1.31");
        doseEntry.getObservation().getCode().setDisplayName(DOSING_NAME);
        doseEntry.getObservation().getCode().setOriginalText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Code.OriginalText());
        doseEntry.getObservation().getCode().getOriginalText().setReference(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Code.OriginalText.Reference());
        doseEntry.getObservation().getCode().getOriginalText().getReference().setValue(DOC_1);
        doseEntry.getObservation().setValue(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Value());
        doseEntry.getObservation().getValue().setValue(healthFoodRecipe.getHealthFood().getDose().shortValue());
        doseEntry.getObservation().getValue().setType("PQ");
        doseEntry.getObservation().getValue().setUnit("U");
        doseEntry.getObservation().getValue().setTranslation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Value.Translation());
        doseEntry.getObservation().getValue().getTranslation().setCode((short) 128);
        doseEntry.getObservation().getValue().getTranslation().setCodeSystem(TRANSLATION_CODE_SYSTEM);
        doseEntry.getObservation().getValue().getTranslation().setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
        doseEntry.getObservation().getValue().getTranslation().setCodeSystemVersion("2.6");
        doseEntry.getObservation().getValue().getTranslation().setDisplayName(VALUE);
        doseEntry.getObservation().getValue().getTranslation().setValue(healthFoodRecipe.getHealthFood().getDose().shortValue());
        recipeSectionEntry.getSubstanceAdministration().getEntryRelationship().add(doseEntry);

        recipeSection.getSection().getEntry().add(recipeSectionEntry);

        clinicalDocument.getComponent().getStructuredBody().getComponent().add(recipeSection);

        return converter.convertObjectToXmlString(clinicalDocument, ClinicalDocument.class, true);
    }

    // Обычный рецепт (не льготный)
    public String createIemkRecipeDocument(FullRecipe fullRecipe, int version) {
        if (fullRecipe.getDrugs() != null && !fullRecipe.getDrugs().isEmpty() && version == 1) {
            return createIemkDrugRecipeDocument(fullRecipe);
        } else if (fullRecipe.getDrugs() != null && !fullRecipe.getDrugs().isEmpty() && version == 2) {
            return createIemkDrugRecipeDocumentNewEdition(fullRecipe, version);
        } else {
            throw new DrugNotFoundException(messageSource.getMessage("drugNotFound", null, Locale.getDefault()));
        }
    }

    @Deprecated
    private String createIemkDrugRecipeDocument(FullRecipe recipe) {
        var clinicalDocument = createIemkClinicalDocumentCommonData("1.2.643.5.1.13.13.14.86.3", "86", RECIPE_TITLE, recipe, false);

        // Тело документа
        clinicalDocument.setComponent(new ClinicalDocument.Component());
        clinicalDocument.getComponent().setStructuredBody(new ClinicalDocument.Component.StructuredBody());

        // Секция сведения о документе
        var documentSection = new ClinicalDocument.Component.StructuredBody.ComponentSection();
        documentSection.setSection(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section());

        // Код секции
        documentSection.getSection().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Code());
        documentSection.getSection().getCode().setCode(DOC_INFO_CODE);
        documentSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
        documentSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
        documentSection.getSection().getCode().setCodeSystemVersion("1.7");
        documentSection.getSection().getCode().setDisplayName(DOC_INFO_NAME);

        documentSection.getSection().setTitle(EL_RECIPE_TITLE);

        // Наполнение секции
        documentSection.getSection().setText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text());
        documentSection.getSection().getText().setTable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table());
        documentSection.getSection().getText().getTable().setTbody(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody());

        // Срок действия рецепта
        var trValidity = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr();
        var tdValidityContent = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr.Td();
        tdValidityContent.getContent().add(recipe.getValidity());
        trValidity.setTh(RECIPE_VALIDITY);
        trValidity.getTd().add(tdValidityContent);
        documentSection.getSection().getText().getTable().getTbody().getTr().add(trValidity);

        documentSection.getSection().getEntry().add(
                createEntry(
                        6004,
                        RECIPE_VALIDITY,
                        null,
                        DictionaryMapper.resolveValidityCode(recipe.getValidity()),
                        RECIPE_VALIDITY_CODE_SYSTEM,
                        RECIPE_VALIDITY,
                        "1.1",
                        recipe.getValidity(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        "CD"
                )
        );

        // Дата окончания действия рецепта
        if (recipe.getEndDate() != null) {
            var tr = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr();
            var tdContent = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr.Td();
            tdContent.getContent().add(recipe.getEndDate().format(DateTimeFormatter.ofPattern(RUS_DATE_FORMAT)));
            tr.setTh(END_DATE_DISPLAY_NAME);
            tr.getTd().add(tdContent);
            documentSection.getSection().getText().getTable().getTbody().getTr().add(tr);

            documentSection.getSection().getEntry().add(
                    createEntry(
                            6005,
                            END_DATE_DISPLAY_NAME,
                            "#DOC_8",
                            null,
                            null,
                            null,
                            null,
                            null,
                            recipe.getEndDate().format(DateTimeFormatter.ofPattern(IEMK_DATE_FORMAT)),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "TS"
                    )
            );
        }

        // По специальному назначению (Отметка)
        documentSection.getSection().getEntry().add(
                createEntry(
                        6006,
                        SPECIAL_NAME,
                        null,
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
                        "BL"
                )
        );

        clinicalDocument.getComponent().getStructuredBody().getComponent().add(documentSection);

        // Секция назначение лекарственного препарата\специализированного продукта лечебного питания
        var recipeSection = new ClinicalDocument.Component.StructuredBody.ComponentSection();
        recipeSection.setSection(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section());

        // Код секции
        recipeSection.getSection().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Code());
        recipeSection.getSection().getCode().setCode(RECIPE);
        recipeSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
        recipeSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
        recipeSection.getSection().getCode().setCodeSystemVersion("1.7");
        recipeSection.getSection().getCode().setDisplayName(RECIPE_NAME);

        recipeSection.getSection().setTitle("Назначение медикамента");

        // Наполнение секции
        recipeSection.getSection().setText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text());
        recipeSection.getSection().getText().setTable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table());
        recipeSection.getSection().getText().getTable().setTbody(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody());
        recipeSection.getSection().getText().getTable().setThead(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Thead());
        recipeSection.getSection().getText().getTable().getThead().setTr(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Thead.Tr());

        recipeSection.getSection().getText().getTable().getThead().getTr().getTh().add(APPOINTED);
        recipeSection.getSection().getText().getTable().getThead().getTr().getTh().add(DOSING_NAME);

        for (var drug : recipe.getDrugs()) {
            var tr = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr();
            tr.getTd().add(createRecipeSectionTd(null, drug.getName() + (drug.getTradeName() != null ? " (торговое наименование " + drug.getTradeName() + ")" : "")));
            if (drug.getDose() != null) {
                tr.getTd().add(createRecipeSectionTd(null, drug.getDose().toString()));
            }
            recipeSection.getSection().getText().getTable().getTbody().getTr().add(tr);
        }

        for (var drug : recipe.getDrugs()) {
            var recipeSectionEntry = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry();
            recipeSectionEntry.setSubstanceAdministration(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration());
            recipeSectionEntry.getSubstanceAdministration().setClassCode(SBADM);
            recipeSectionEntry.getSubstanceAdministration().setMoodCode("RQO");

            if (recipe.getBenefit() != null) {
                // Кодирование типов назначений льготного рецепта
                recipeSectionEntry.getSubstanceAdministration().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Code());
                recipeSectionEntry.getSubstanceAdministration().getCode().setCode(recipe.getBenefit().getTypeCode().shortValue());
                recipeSectionEntry.getSubstanceAdministration().getCode().setCodeSystem(RECIPE_TYPE_CODE_SYSTEM);
                recipeSectionEntry.getSubstanceAdministration().getCode().setCodeSystemName(RECIPE_TYPE_CODE_SYSTEM_NAME);
                recipeSectionEntry.getSubstanceAdministration().getCode().setCodeSystemVersion("1.2");
                recipeSectionEntry.getSubstanceAdministration().getCode().setDisplayName(recipe.getBenefit().getTypeName());
            }

            // Наполнение секции
            recipeSectionEntry.getSubstanceAdministration().setText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Text());
            recipeSectionEntry.getSubstanceAdministration().getText().setReference(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Text.Reference());

            // Длительность приема препарата
            if (drug.getPeriodCode() != null) {
                recipeSectionEntry.getSubstanceAdministration().setEffectiveTime(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EffectiveTime());
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().setType(IVL_TS);
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().setWidth(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EffectiveTime.Width());
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().setUnit(drug.getPeriodUnit());
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().setValue(drug.getDurationDays());
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().setTranslation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EffectiveTime.Width.Translation());
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCode(drug.getPeriodCode());
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setValue(drug.getDurationDays());
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCodeSystem(TRANSLATION_CODE_SYSTEM);
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setCodeSystemVersion("2.6");
                recipeSectionEntry.getSubstanceAdministration().getEffectiveTime().getWidth().getTranslation().setDisplayName(drug.getPeriodName());
            }

            // Путь введения препарата
            if (drug.getRouteCode() != null && drug.getRouteName() != null) {
                recipeSectionEntry.getSubstanceAdministration().setRouteCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.RouteCode());
                recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCode(drug.getRouteCode().shortValue());
                recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystem(ROUTE_CODE_SYSTEM);
                recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemName(ROUTE_CODE_SYSTEM_NAME);
                recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemVersion("1.2");
                recipeSectionEntry.getSubstanceAdministration().getRouteCode().setDisplayName(drug.getRouteName());
            }

            // Назначенный препарат\специализированный продукт лечебного питания
            recipeSectionEntry.getSubstanceAdministration().setConsumable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable());
            recipeSectionEntry.getSubstanceAdministration().getConsumable().setTypeCode("CSM");
            recipeSectionEntry.getSubstanceAdministration().getConsumable().setManufacturedProduct(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable.ManufacturedProduct());
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setClassCode("MANU");
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setManufacturedMaterial(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable.ManufacturedProduct.ManufacturedMaterial());
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setClassCode("MMAT");
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setDeterminerCode("KIND");
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.Consumable.ManufacturedProduct.ManufacturedMaterial.Code());
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCode(drug.getCodeMnn());
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystem(MATERIAL_CODE_SYSTEM);
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemName(MATERIAL_CODE_SYSTEM_NAME);
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemVersion("5.36");
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setDisplayName(drug.getName());
            if (drug.getTradeName() != null) {
                recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setName(drug.getTradeName());
            }

            // Инструкция по применению препарата
            if (drug.getSingleDose() != null) {
                var instructionEntry = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship();
                instructionEntry.setTypeCode("COMP");
                instructionEntry.setSubstanceAdministration(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.EntryRelationshipSubstanceAdministration());
                instructionEntry.getSubstanceAdministration().setClassCode("SBADM");
                instructionEntry.getSubstanceAdministration().setMoodCode("RQO");
                instructionEntry.getSubstanceAdministration().setEffectiveTime(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.EntryRelationshipSubstanceAdministration.EffectiveTime());
                instructionEntry.getSubstanceAdministration().getEffectiveTime().setType("PIVL_TS");
                instructionEntry.getSubstanceAdministration().getEffectiveTime().setInstitutionSpecified(false);
                instructionEntry.getSubstanceAdministration().getEffectiveTime().setPeriod(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.EntryRelationshipSubstanceAdministration.EffectiveTime.Period());
                instructionEntry.getSubstanceAdministration().getEffectiveTime().getPeriod().setValue(drug.getPeriod());
                instructionEntry.getSubstanceAdministration().getEffectiveTime().getPeriod().setUnit(drug.getPeriodUnit());
                instructionEntry.getSubstanceAdministration().getEffectiveTime().getPeriod().setTranslation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.EntryRelationshipSubstanceAdministration.EffectiveTime.Period.Translation());
                instructionEntry.getSubstanceAdministration().getEffectiveTime().getPeriod().getTranslation().setCode(drug.getPeriodCode());
                instructionEntry.getSubstanceAdministration().getEffectiveTime().getPeriod().getTranslation().setValue(drug.getPeriod());
                instructionEntry.getSubstanceAdministration().getEffectiveTime().getPeriod().getTranslation().setCodeSystem("1.2.643.5.1.13.13.11.1358");
                instructionEntry.getSubstanceAdministration().getEffectiveTime().getPeriod().getTranslation().setCodeSystemName("Единицы измерения");
                instructionEntry.getSubstanceAdministration().getEffectiveTime().getPeriod().getTranslation().setDisplayName(drug.getPeriodName());
                instructionEntry.getSubstanceAdministration().getEffectiveTime().getPeriod().getTranslation().setCodeSystemVersion("2.5");
                instructionEntry.getSubstanceAdministration().setConsumable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.EntryRelationshipSubstanceAdministration.Consumable());
                instructionEntry.getSubstanceAdministration().getConsumable().setManufacturedProduct(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.EntryRelationshipSubstanceAdministration.Consumable.ManufacturedProduct());
                instructionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().setManufacturedMaterial(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.EntryRelationshipSubstanceAdministration.Consumable.ManufacturedProduct.ManufacturedMaterial());
                instructionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setNullFlavor("NA");
                instructionEntry.getSubstanceAdministration().setDoseQuantity(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.EntryRelationshipSubstanceAdministration.DoseQuantity());
                instructionEntry.getSubstanceAdministration().getDoseQuantity().setType("IVL_PQ");
                instructionEntry.getSubstanceAdministration().getDoseQuantity().setUnit("{" + drug.getSingleDoseUnit() + "}");
                instructionEntry.getSubstanceAdministration().getDoseQuantity().setValue(drug.getSingleDose());
                instructionEntry.getSubstanceAdministration().getDoseQuantity().setTranslation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.EntryRelationshipSubstanceAdministration.DoseQuantity.Translation());
                instructionEntry.getSubstanceAdministration().getDoseQuantity().getTranslation().setCode(drug.getSingleDoseCode());
                instructionEntry.getSubstanceAdministration().getDoseQuantity().getTranslation().setValue(drug.getSingleDose());
                instructionEntry.getSubstanceAdministration().getDoseQuantity().getTranslation().setCodeSystem("1.2.643.5.1.13.13.99.2.612");
                instructionEntry.getSubstanceAdministration().getDoseQuantity().getTranslation().setCodeSystemName("Потребительские единицы ЕСКЛП");
                instructionEntry.getSubstanceAdministration().getDoseQuantity().getTranslation().setDisplayName(drug.getSingleDoseName());
                instructionEntry.getSubstanceAdministration().getDoseQuantity().getTranslation()
                        .setCodeSystemVersion("2.30");
                recipeSectionEntry.getSubstanceAdministration().getEntryRelationship().add(instructionEntry);
            }

            // Количество назначенных доз
            if (drug.getDose() != null) {
                var doseEntry = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship();
                doseEntry.setTypeCode("COMP");
                doseEntry.setObservation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation());
                doseEntry.getObservation().setClassCode("OBS");
                doseEntry.getObservation().setMoodCode("EVN");
                doseEntry.getObservation().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Code());
                doseEntry.getObservation().getCode().setCode(6011);
                doseEntry.getObservation().getCode().setCodeSystem(OBSERVATION_CODE_SYSTEM);
                doseEntry.getObservation().getCode().setCodeSystemName(OBSERVATION_CODE_SYSTEM_NAME);
                doseEntry.getObservation().getCode().setCodeSystemVersion("1.31");
                doseEntry.getObservation().getCode().setDisplayName(DOSING_NAME);
                doseEntry.getObservation().getCode().setOriginalText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Code.OriginalText());
                doseEntry.getObservation().getCode().getOriginalText().setReference(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Code.OriginalText.Reference());
                doseEntry.getObservation().getCode().getOriginalText().getReference().setValue(DOC_1);
                doseEntry.getObservation().setValue(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Value());
                doseEntry.getObservation().getValue().setValue(drug.getDose().shortValue());
                doseEntry.getObservation().getValue().setType("PQ");
                doseEntry.getObservation().getValue().setUnit("U");
                doseEntry.getObservation().getValue().setTranslation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.SubstanceAdministration.EntryRelationship.Observation.Value.Translation());
                doseEntry.getObservation().getValue().getTranslation().setCode((short) 128);
                doseEntry.getObservation().getValue().getTranslation().setCodeSystem(TRANSLATION_CODE_SYSTEM);
                doseEntry.getObservation().getValue().getTranslation().setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
                doseEntry.getObservation().getValue().getTranslation().setCodeSystemVersion("2.6");
                doseEntry.getObservation().getValue().getTranslation().setDisplayName("Ед");
                doseEntry.getObservation().getValue().getTranslation().setValue(drug.getDose().shortValue());
                recipeSectionEntry.getSubstanceAdministration().getEntryRelationship().add(doseEntry);
            }

            recipeSection.getSection().getEntry().add(recipeSectionEntry);
        }

        clinicalDocument.getComponent().getStructuredBody().getComponent().add(recipeSection);

        return converter.convertObjectToXmlString(clinicalDocument, ClinicalDocument.class, true);
    }

    private String createIemkDrugRecipeDocumentNewEdition(FullRecipe recipe, int version) {
        var clinicalDocument = createIemkClinicalDocumentCommonDataNewEdition(recipe, version, false);

        if (version > 1) {
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
        recipeSection.getSection().getCode().setCodeSystemVersion("2.1");
        recipeSection.getSection().getCode().setDisplayName(RECIPE_NAME);

        recipeSection.getSection().setTitle(RECIPE_SECTION_TITLE);

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

        for (var drug : recipe.getDrugs()) {
            var thBodyDrug = new StrucDocTd();
            var thBodyDose = new StrucDocTd();
            thBodyDrug.getContent().add(drug.getName() + (drug.getTradeName() != null ? " (торговое наименование " + drug.getTradeName() + ")" : ""));
            if (drug.getDose() != null) {
                thBodyDose.getContent().add(drug.getDose().toString());
            }
            body.getThOrTd().add(thBodyDrug);
            body.getThOrTd().add(thBodyDose);
        }

        strucDocTbody.getTr().add(body);

        for (var drug : recipe.getDrugs()) {
            var recipeSectionEntry = new POCDMT000040Entry();
            recipeSectionEntry.setSubstanceAdministration(new POCDMT000040SubstanceAdministration());
            recipeSectionEntry.getSubstanceAdministration().getClassCode().add(SBADM);
            recipeSectionEntry.getSubstanceAdministration().setMoodCode(XDocumentSubstanceMood.RQO);

            recipeSectionEntry.getSubstanceAdministration().setCode(new POCDMT000040SubstanceAdministration.Code());
            recipeSectionEntry.getSubstanceAdministration().getCode().getNullFlavor().add("NI");

            // Длительность приема препарата
            if (drug.getDurationDays() != null) {
                var ivlts = new IVLTS();
                var width = new PQ();
                width.setValue(drug.getDurationDays().toString());
                width.setUnit("d");
                var pqr = new PQR();
                pqr.setValue(drug.getDurationDays().toString());
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
            if (drug.getRouteCode() != null && drug.getRouteName() != null) {
                recipeSectionEntry.getSubstanceAdministration().setRouteCode(new POCDMT000040SubstanceAdministration.RouteCode());
                recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCode(drug.getRouteCode().toString());
                recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystem(ROUTE_CODE_SYSTEM);
                recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemName(ROUTE_CODE_SYSTEM_NAME);
                recipeSectionEntry.getSubstanceAdministration().getRouteCode().setCodeSystemVersion("2.3");
                recipeSectionEntry.getSubstanceAdministration().getRouteCode().setDisplayName(drug.getRouteName());
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
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCode(drug.getCodeMnn());
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystem(MATERIAL_CODE_SYSTEM);
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemName(MATERIAL_CODE_SYSTEM_NAME);
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setCodeSystemVersion("5.36");
            recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().setDisplayName(drug.getName());
            if (drug.getTradeName() != null) {
                recipeSectionEntry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().setName(drug.getTradeName());
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

            if (drug.getDose() != null) {
                var pq = new PQ();
                pq.setValue(drug.getDose().toString());
                pq.setUnit("U");
                var translation = new PQR();
                translation.setValue(drug.getDose().toString());
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
        }

        strucDocTable.getTbody().add(strucDocTbody);
        strucDocTable.setThead(strucDocThead);
        recipeSection.getSection().getText().getContent().add(new JAXBElement(new QName(URN_HL_7_ORG_V_3, TABLE), StrucDocTable.class, strucDocTable));
        clinicalDocument.getComponent().getStructuredBody().getComponent().add(recipeSection);
        return converter.convertObjectToXmlString(clinicalDocument, POCDMT000040ClinicalDocument.class, true);
    }

    private ClinicalDocument createIemkClinicalDocumentCommonData(String templateId, String docTypeCode, String displayName, Recipe recipe, boolean preferential) {
        var clinicalDocument = new ClinicalDocument();

        // Область применения документа (Страна)
        clinicalDocument.setRealmCode(new ClinicalDocument.RealmCode());
        clinicalDocument.getRealmCode().setCode("RU");

        // Указатель на использование CDA R2
        clinicalDocument.setTypeId(new ClinicalDocument.TypeId());
        clinicalDocument.getTypeId().setRoot(CODING_SCHEME_VALUE);
        clinicalDocument.getTypeId().setExtension("POCD_MT000040");

        // Идентификатор Шаблона документа "Льготный рецепт на лекарственный препарат, изделие медицинского назначения и специализированный продукт лечебного питания. Третий уровень формализации."
        // по справочнику "Справочник шаблонов CDA документов" (OID: 1.2.643.5.1.13.13.99.2.267)
        clinicalDocument.setTemplateId(new ClinicalDocument.TemplateId());
        clinicalDocument.getTemplateId().setRoot(templateId);

        // Уникальный идентификатор документа
        // по правилу: root = OID_медицинской_организации.100.НомерМИС.НомерЭкзМИС.51 extension = идентификатор документа
        clinicalDocument.setId(new ClinicalDocument.Id());
        clinicalDocument.getId().setRoot(recipe.getProviderOrganization().getOid() + ".100.1.1.51");
        clinicalDocument.getId().setExtension(recipe.getMisId());

        // Тип документа
        clinicalDocument.setCode(new ClinicalDocument.Code());
        clinicalDocument.getCode().setCode(docTypeCode);
        clinicalDocument.getCode().setCodeSystem("1.2.643.5.1.13.13.11.1522");
        clinicalDocument.getCode().setCodeSystemName("Виды медицинской документации");
        clinicalDocument.getCode().setCodeSystemVersion("4.6");
        clinicalDocument.getCode().setDisplayName(displayName);

        // Заголовок документа
        clinicalDocument.setTitle(recipe.getName());

        // Дата создания документа (Должен быть с точностью до дня, но следует быть с точностью до минут)
        clinicalDocument.setEffectiveTime(new ClinicalDocument.EffectiveTime());
        clinicalDocument.getEffectiveTime().setValue(recipe.getCreateDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));

        // Уровень конфиденциальности медицинского документа
        clinicalDocument.setConfidentialityCode(new ClinicalDocument.ConfidentialityCode());
        clinicalDocument.getConfidentialityCode().setCode("N");
        clinicalDocument.getConfidentialityCode().setCodeSystem("1.2.643.5.1.13.13.99.2.285");
        clinicalDocument.getConfidentialityCode().setCodeSystemVersion("1.1");
        clinicalDocument.getConfidentialityCode().setCodeSystemName("Уровень конфиденциальности медицинского документа");
        clinicalDocument.getConfidentialityCode().setDisplayName("обычный");

        // Язык документа
        clinicalDocument.setLanguageCode(new ClinicalDocument.LanguageCode());
        clinicalDocument.getLanguageCode().setCode(RU_RU);

        // Уникальный идентификатор документа
        // по правилу: root = OID_медицинской_организации.100.НомерМИС.НомерЭкзМИС.50 extension = идентификатор набора версий документа
        clinicalDocument.setSetId(new ClinicalDocument.SetId());
        clinicalDocument.getSetId().setRoot(recipe.getProviderOrganization().getOid() + ".100.1.1.50");
        clinicalDocument.getSetId().setExtension(recipe.getVersionId());

        // Номер версии данного документа
        clinicalDocument.setVersionNumber(new ClinicalDocument.VersionNumber());
        clinicalDocument.getVersionNumber().setValue(recipe.getVersionId().shortValue());

        // Данные о пациенте
        clinicalDocument.setRecordTarget(new ClinicalDocument.RecordTarget());
        clinicalDocument.getRecordTarget().setPatientRole(new ClinicalDocument.RecordTarget.PatientRole());
        clinicalDocument.getRecordTarget().getPatientRole().setPatient(new ClinicalDocument.RecordTarget.PatientRole.Patient());
        clinicalDocument.getRecordTarget().getPatientRole().setProviderOrganization(new ClinicalDocument.RecordTarget.PatientRole.ProviderOrganization());

        // Уникальный идентификатор пациента в МИС
        // по правилу: root = OID_медицинской_организации.100.НомерМИС.НомерЭкзМИС.10 extension = идентификатор пациента
        var patientId = new ClinicalDocument.RecordTarget.PatientRole.Id();
        patientId.setRoot(recipe.getProviderOrganization().getOid() + ".100.1.1.10");
        patientId.setExtension(recipe.getPatient().getMisId());
        clinicalDocument.getRecordTarget().getPatientRole().getId().add(patientId);

        // СНИЛС пациента
        var patientSnils = new ClinicalDocument.RecordTarget.PatientRole.Id();
        patientSnils.setRoot(SNILS_ROOT);
        patientSnils.setExtension(recipe.getPatient().getSnils());
        clinicalDocument.getRecordTarget().getPatientRole().getId().add(patientSnils);

        // Документ, удостоверяющий личность получателя, серия, номер, кем выдан
        // Тип документа
        clinicalDocument.getRecordTarget().getPatientRole().setIdentityDoc(new IdentityDoc());
        if (recipe.getPatient().getIdentityDoc() != null) {
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setIdentityCardTypeId(new IdentityDoc.IdentityCardTypeId());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardTypeId().setCode((short) 1);
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardTypeId().setType("CD");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardTypeId().setCodeSystem("1.2.643.5.1.13.13.99.2.48");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardTypeId().setCodeSystemName("Документы, удостоверяющие личность");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardTypeId().setCodeSystemVersion(BigDecimal.valueOf(4.2));
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIdentityCardTypeId().setDisplayName("Паспорт гражданина РФ");

            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setSeries(new IdentityDocElement());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getSeries().setValue(recipe.getPatient().getIdentityDoc().getSeries());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getSeries().setType("ST");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setNumber(new IdentityDocElement());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getNumber().setValue(recipe.getPatient().getIdentityDoc().getNumber());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getNumber().setType("ST");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setIssueOrgCode(new IdentityDocElement());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIssueOrgCode().setValue(recipe.getPatient().getIdentityDoc().getIssueOrgCode());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIssueOrgCode().setType("ST");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setIssueOrgName(new IdentityDocElement());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIssueOrgName().setValue(recipe.getPatient().getIdentityDoc().getIssueOrgName());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIssueOrgName().setType("ST");
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().setIssueDate(new IdentityDoc.IssueDate());
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIssueDate().setValue(Long.parseLong(recipe.getPatient().getIdentityDoc().getIssueDate().format(DateTimeFormatter.ofPattern(IEMK_DATE_FORMAT))));
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIssueDate().setType("TS");
        } else {
            clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getNullFlavor().add("NI");
        }

        // Адрес постоянной регистрации пациента
        var patientAddress = new ClinicalDocument.RecordTarget.PatientRole.Addr();
        if (recipe.getPatient().getAddress() != null) {
            patientAddress.setUse("H");
            patientAddress.setState(Integer.parseInt(recipe.getPatient().getAddress().getRegionCode()));
            patientAddress.setStreetAddressLine(recipe.getPatient().getAddress().getFullAddress());
            patientAddress.setAddress(new Address());
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
        clinicalDocument.getRecordTarget().getPatientRole().getPatient().setName(new ClinicalDocument.RecordTarget.PatientRole.Patient.Name());
        clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().setFamily(recipe.getPatient().getLastName());
        clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().getGiven().add(recipe.getPatient().getFirstName());
        if (recipe.getPatient().getMiddleName() != null) {
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().getGiven().add(recipe.getPatient().getMiddleName());
        }
        if (recipe.getPatient().getBirthDate() != null) {
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().setBirthTime(new ClinicalDocument.RecordTarget.PatientRole.Patient.BirthTime());
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getBirthTime().setValue(Long.parseLong(recipe.getPatient().getBirthDate().format(DateTimeFormatter.ofPattern(IEMK_DATE_FORMAT))));
        }
        if (recipe.getPatient().getGender() != null) {
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().setAdministrativeGenderCode(new ClinicalDocument.RecordTarget.PatientRole.Patient.AdministrativeGenderCode());
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().setCode(recipe.getPatient().getGender().equals("М") ? (short) 1 : (short) 2);
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().setCodeSystem("1.2.643.5.1.13.13.11.1040");
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().setCodeSystemName("Пол пациента");
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().setCodeSystemVersion("2.1");
            clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().setDisplayName(recipe.getPatient().getGender().equals("М") ? "Мужской" : "Женский");
        }

        // Медицинская организация, оформившая рецепт
        clinicalDocument.getRecordTarget().getPatientRole().setProviderOrganization(new ClinicalDocument.RecordTarget.PatientRole.ProviderOrganization());
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().setId(new ClinicalDocument.RecordTarget.PatientRole.ProviderOrganization.Id());
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getId().setRoot(recipe.getProviderOrganization().getOid());
        if (!preferential) {
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().setLicenseNumber(new ClinicalDocument.RecordTarget.PatientRole.ProviderOrganization.Id());
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getLicenseNumber().setNullFlavor("NA");
        }
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().setOgrn(BigInteger.valueOf(Long.parseLong(recipe.getProviderOrganization().getOgrn())));
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().setOgrnip(new Ogrnip());
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getOgrnip().setNullFlavor("NA");
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().setName(recipe.getProviderOrganization().getName());
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().setTelecom(new ClinicalDocument.RecordTarget.PatientRole.ProviderOrganization.Telecom());
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getTelecom().setUse("WP");
        clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getTelecom().setValue("tel:" + recipe.getProviderOrganization().getPhone());
        if (recipe.getProviderOrganization().getAddress() != null) {
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().setAddr(new ClinicalDocument.RecordTarget.PatientRole.ProviderOrganization.Addr());
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().setState((short) Integer.parseInt(recipe.getProviderOrganization().getAddress().getRegionCode()));
            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().setStreetAddressLine(recipe.getProviderOrganization().getAddress().getFullAddress());

            clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().setAddress(new Address());
            if (recipe.getProviderOrganization().getAddress().getAoGuid() != null && recipe.getProviderOrganization().getAddress().getHouseGuid() != null) {
                clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getAddress().setAOGUID(recipe.getProviderOrganization().getAddress().getAoGuid());
                clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getAddress().setHOUSEGUID(recipe.getProviderOrganization().getAddress().getHouseGuid());
            } else {
                clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr().getAddress().getNullFlavor().add("NI");
            }
        }

        // Данные об авторе документа
        clinicalDocument.setAuthor(new ClinicalDocument.Author());
        clinicalDocument.getAuthor().setAssignedAuthor(new ClinicalDocument.Author.AssignedAuthor());

        // Дата подписи документа автором
        clinicalDocument.getAuthor().setTime(new ClinicalDocument.Author.Time());
        clinicalDocument.getAuthor().getTime().setValue(recipe.getSignatureDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));

        // Уникальный идентификатор автора в МИС
        var authorId = new ClinicalDocument.Author.AssignedAuthor.Id();
        authorId.setRoot(recipe.getAuthor().getRepresentedOrganisation().getOid() + OID_ROOT);
        authorId.setExtension(recipe.getAuthor().getMisId());
        clinicalDocument.getAuthor().getAssignedAuthor().getId().add(authorId);

        // Телефон автора
        var authorPhone = new ClinicalDocument.Author.AssignedAuthor.Telecom();
        if (recipe.getAuthor().getPhone() != null) {
            authorPhone.setValue(recipe.getAuthor().getPhone());
        } else {
            authorPhone.setValue("NA");
        }
        clinicalDocument.getAuthor().getAssignedAuthor().getTelecom().add(authorPhone);

        // СНИЛС автора
        var authorSnils = new ClinicalDocument.Author.AssignedAuthor.Id();
        authorSnils.setRoot(SNILS_ROOT);
        authorSnils.setExtension(recipe.getAuthor().getSnils());
        clinicalDocument.getAuthor().getAssignedAuthor().getId().add(authorSnils);

        // Код должности автора
        clinicalDocument.getAuthor().getAssignedAuthor().setCode(new ClinicalDocument.Author.AssignedAuthor.Code());
        clinicalDocument.getAuthor().getAssignedAuthor().getCode().setCode(recipe.getAuthor().getPost().getCode());
        clinicalDocument.getAuthor().getAssignedAuthor().getCode().setCodeSystem(POST_CODE_SYSTEM);
        clinicalDocument.getAuthor().getAssignedAuthor().getCode().setCodeSystemName(POST_CODE_SYSTEM_NAME);
        clinicalDocument.getAuthor().getAssignedAuthor().getCode().setCodeSystemVersion("6.4");
        clinicalDocument.getAuthor().getAssignedAuthor().getCode().setDisplayName(recipe.getAuthor().getPost().getName());

        // ФИО
        clinicalDocument.getAuthor().getAssignedAuthor().setAssignedPerson(new ClinicalDocument.Author.AssignedAuthor.AssignedPerson());
        clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().setName(new ClinicalDocument.Author.AssignedAuthor.AssignedPerson.Name());
        clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().setFamily(recipe.getAuthor().getLastName());
        clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().getGiven().add(recipe.getAuthor().getFirstName());
        if (recipe.getAuthor().getMiddleName() != null) {
            clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().getGiven().add(recipe.getAuthor().getMiddleName());
        }

        // Место работы автора
        clinicalDocument.getAuthor().getAssignedAuthor().setRepresentedOrganization(new ClinicalDocument.Author.AssignedAuthor.RepresentedOrganization());
        clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().setClassCode("ORG");
        clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().setId(new ClinicalDocument.Author.AssignedAuthor.RepresentedOrganization.Id());
        clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getId().setRoot(recipe.getAuthor().getRepresentedOrganisation().getOid());
        clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().setName(recipe.getAuthor().getRepresentedOrganisation().getName());
        if (recipe.getAuthor().getRepresentedOrganisation().getAddress() != null) {
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().setAddr(new ClinicalDocument.Author.AssignedAuthor.RepresentedOrganization.Addr());
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().setState((short) Integer.parseInt(recipe.getAuthor().getRepresentedOrganisation().getAddress().getRegionCode()));
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().setStreetAddressLine(recipe.getAuthor().getRepresentedOrganisation().getAddress().getFullAddress());
            clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().setAddress(new Address());
            if (recipe.getAuthor().getRepresentedOrganisation().getAddress().getAoGuid() != null && recipe.getAuthor().getRepresentedOrganisation().getAddress().getHouseGuid() != null) {
                clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getAddress().setAOGUID(recipe.getAuthor().getRepresentedOrganisation().getAddress().getAoGuid());
                clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getAddress().setHOUSEGUID(recipe.getAuthor().getRepresentedOrganisation().getAddress().getHouseGuid());
            } else {
                clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr().getAddress().getNullFlavor().add("NI");
            }
        }

        // Данные об организации владельце документа
        clinicalDocument.setCustodian(new ClinicalDocument.Custodian());
        clinicalDocument.getCustodian().setAssignedCustodian(new ClinicalDocument.Custodian.AssignedCustodian());
        clinicalDocument.getCustodian().getAssignedCustodian().setRepresentedCustodianOrganization(new ClinicalDocument.Custodian.AssignedCustodian.RepresentedCustodianOrganization());
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().setId(new ClinicalDocument.Custodian.AssignedCustodian.RepresentedCustodianOrganization.Id());
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getId().setRoot(recipe.getCustodianOrganisation().getOid());
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().setName(recipe.getCustodianOrganisation().getName());
        if (recipe.getCustodianOrganisation().getAddress() != null) {
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().setAddr(new ClinicalDocument.Custodian.AssignedCustodian.RepresentedCustodianOrganization.Addr());
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().setState((short) Integer.parseInt(recipe.getCustodianOrganisation().getAddress().getRegionCode()));
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().setStreetAddressLine(recipe.getCustodianOrganisation().getAddress().getFullAddress());
            clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().setAddress(new Address());
            if (recipe.getCustodianOrganisation().getAddress().getAoGuid() != null && recipe.getCustodianOrganisation().getAddress().getHouseGuid() != null) {
                clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getAddress().setAOGUID(recipe.getCustodianOrganisation().getAddress().getAoGuid());
                clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getAddress().setHOUSEGUID(recipe.getCustodianOrganisation().getAddress().getHouseGuid());
            } else {
                clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr().getAddress().getNullFlavor().add("NI");
            }
        }
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().setTelecom(new ClinicalDocument.Custodian.AssignedCustodian.RepresentedCustodianOrganization.Telecom());
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getTelecom().setUse("WP");
        clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getTelecom().setValue("NA");

        // Данные о получателе документа МЗ РФ
        clinicalDocument.setInformationRecipient(new ClinicalDocument.InformationRecipient());
        clinicalDocument.getInformationRecipient().setIntendedRecipient(new ClinicalDocument.InformationRecipient.IntendedRecipient());
        clinicalDocument.getInformationRecipient().getIntendedRecipient().setReceivedOrganization(new ClinicalDocument.InformationRecipient.IntendedRecipient.ReceivedOrganization());
        clinicalDocument.getInformationRecipient().getIntendedRecipient().getReceivedOrganization().setId(new ClinicalDocument.InformationRecipient.IntendedRecipient.ReceivedOrganization.Id());
        clinicalDocument.getInformationRecipient().getIntendedRecipient().getReceivedOrganization().getId().setRoot("1.2.643.5.1.13");
        clinicalDocument.getInformationRecipient().getIntendedRecipient().getReceivedOrganization().setName("Министерство здравоохранения Российской Федерации");

        // Данные о лице придавшем юридическую силу документу
        clinicalDocument.setLegalAuthenticator(new ClinicalDocument.LegalAuthenticator());

        // Дата подписи документа лицом, придавшем юридическую силу документу
        clinicalDocument.getLegalAuthenticator().setTime(new ClinicalDocument.LegalAuthenticator.Time());
        clinicalDocument.getLegalAuthenticator().getTime().setValue(recipe.getLegalAuthenticatorSignatureDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
        clinicalDocument.getLegalAuthenticator().setSignatureCode(new ClinicalDocument.LegalAuthenticator.SignatureCode());

        // Факт наличия подписи на документе
        clinicalDocument.getLegalAuthenticator().getSignatureCode().setCode("S");

        // Лицо, придавшее юридическую силу документу (роль)
        clinicalDocument.getLegalAuthenticator().setAssignedEntity(new ClinicalDocument.LegalAuthenticator.AssignedEntity());

        // Уникальный идентификатор лица придавшего юридическую силу документу в МИС
        var legalAuthenticatorId = new ClinicalDocument.LegalAuthenticator.AssignedEntity.Id();
        legalAuthenticatorId.setRoot(recipe.getLegalAuthenticator().getRepresentedOrganisation().getOid() + OID_ROOT);
        legalAuthenticatorId.setExtension(recipe.getLegalAuthenticator().getMisId());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getId().add(legalAuthenticatorId);

        // СНИЛС лица придавшего юридическую силу документу
        var legalAuthenticatorSnils = new ClinicalDocument.LegalAuthenticator.AssignedEntity.Id();
        legalAuthenticatorSnils.setRoot(SNILS_ROOT);
        legalAuthenticatorSnils.setExtension(recipe.getLegalAuthenticator().getSnils());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getId().add(legalAuthenticatorSnils);

        // Код должности лица, придавшего юридическую силу документу
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().setCode(new ClinicalDocument.LegalAuthenticator.AssignedEntity.Code());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().setCode(recipe.getLegalAuthenticator().getPost().getCode().shortValue());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().setCodeSystem(POST_CODE_SYSTEM);
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().setCodeSystemName(POST_CODE_SYSTEM_NAME);
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().setCodeSystemVersion("6.4");
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().setDisplayName(recipe.getLegalAuthenticator().getPost().getName());

        // ФИО
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().setAssignedPerson(new ClinicalDocument.LegalAuthenticator.AssignedEntity.AssignedPerson());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().setName(new ClinicalDocument.LegalAuthenticator.AssignedEntity.AssignedPerson.Name());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().setFamily(recipe.getLegalAuthenticator().getLastName());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().getGiven().add(recipe.getLegalAuthenticator().getFirstName());
        if (recipe.getLegalAuthenticator().getMiddleName() != null) {
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().getGiven().add(recipe.getLegalAuthenticator().getMiddleName());
        }

        // Место работы лица, придавшего юридическую силу документу
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().setRepresentedOrganization(new ClinicalDocument.LegalAuthenticator.AssignedEntity.RepresentedOrganization());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().setClassCode("ORG");
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().setId(new ClinicalDocument.LegalAuthenticator.AssignedEntity.RepresentedOrganization.Id());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getId().setRoot(recipe.getLegalAuthenticator().getRepresentedOrganisation().getOid());
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().setName(recipe.getLegalAuthenticator().getRepresentedOrganisation().getName());
        if (recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress() != null) {
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().setAddr(new ClinicalDocument.LegalAuthenticator.AssignedEntity.RepresentedOrganization.Addr());
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().setState((short) Integer.parseInt(recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getRegionCode()));
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().setStreetAddressLine(recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getFullAddress());
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().setAddress(new Address());
            if (recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getAoGuid() != null && recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getHouseGuid() != null) {
                clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getAddress().setAOGUID(recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getAoGuid());
                clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getAddress().setHOUSEGUID(recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().getHouseGuid());
            } else {
                clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr().getAddress().getNullFlavor().add("NI");
            }
        }

        // Сведения о страховом полисе ОМС
        if (recipe.getPatient().getInsurance() != null && !"86".equals(docTypeCode)) {
            clinicalDocument.setParticipant(new ClinicalDocument.Participant());
            clinicalDocument.getParticipant().setTypeCode("HLD");
            clinicalDocument.getParticipant().setAssociatedEntity(new ClinicalDocument.Participant.AssociatedEntity());
            clinicalDocument.getParticipant().getAssociatedEntity().setId(new ClinicalDocument.Participant.AssociatedEntity.Id());
            clinicalDocument.getParticipant().getAssociatedEntity().getId().setRoot("1.2.643.5.1.13.2.7.100.2");
            clinicalDocument.getParticipant().getAssociatedEntity().getId().setExtension(BigInteger.valueOf(Long.parseLong(recipe.getPatient().getInsurance().getNumber())));
            clinicalDocument.getParticipant().getAssociatedEntity().setCode(new ClinicalDocument.Participant.AssociatedEntity.Code());
            clinicalDocument.getParticipant().getAssociatedEntity().getCode().setCode("SELF");
            clinicalDocument.getParticipant().getAssociatedEntity().getCode().setCodeSystem("2.16.840.1.113883.5.111");
            clinicalDocument.getParticipant().getAssociatedEntity().setClassCode("POLHOLD");
            clinicalDocument.getParticipant().getAssociatedEntity().setScopingOrganization(new ClinicalDocument.Participant.AssociatedEntity.ScopingOrganization());

            // Уникальный идентификатор страховой компании
            clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().setId(new ClinicalDocument.Participant.AssociatedEntity.ScopingOrganization.Id());
            clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getId().setRoot("1.2.643.5.1.13.13.99.2.183");
            clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getId().setExtension(recipe.getPatient().getInsurance().getInsuranceOrganisation().getNsiId().shortValue());

            // Наименование страховой компании
            clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().setName(recipe.getPatient().getInsurance().getInsuranceOrganisation().getName());

            // Телефон страховой компании
            clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().setTelecom(new ClinicalDocument.Participant.AssociatedEntity.ScopingOrganization.Telecom());
            clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getTelecom().setValue("tel:" + recipe.getPatient().getInsurance().getInsuranceOrganisation().getPhone());

            //  Адрес страховой компании
            if (recipe.getPatient().getInsurance() != null && recipe.getPatient().getInsurance().getInsuranceOrganisation() != null && recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress() != null) {
                clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().setAddr(new ClinicalDocument.Participant.AssociatedEntity.ScopingOrganization.Addr());
                clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getAddr().setState((short) Integer.parseInt(recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress().getRegionCode()));
                clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getAddr().setStreetAddressLine(recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress().getFullAddress());
                clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getAddr().setAddress(new Address());
                if (recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress().getAoGuid() != null && recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress().getHouseGuid() != null) {
                    clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getAddr().getAddress().setAOGUID(recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress().getAoGuid());
                    clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getAddr().getAddress().setHOUSEGUID(recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress().getHouseGuid());
                } else {
                    clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getAddr().getAddress().getNullFlavor().add("NI");
                }
            }
        }
        return clinicalDocument;
    }

    private void createPreferentialRecipeCommonData(Recipe recipe, ClinicalDocument clinicalDocument) {
        // Сведения о случае оказания мед. помощи
        clinicalDocument.setComponentOf(new ClinicalDocument.ComponentOf());
        clinicalDocument.getComponentOf().setEncompassingEncounter(new ClinicalDocument.ComponentOf.EncompassingEncounter());
        clinicalDocument.getComponentOf().getEncompassingEncounter().setEffectiveTime(new ClinicalDocument.ComponentOf.EncompassingEncounter.EffectiveTime());

        if (recipe.getMedicalCareCase() != null) {
            // Уникальный идентификатор случая оказания медицинской помощи
            var medicalCaseId = new ClinicalDocument.ComponentOf.EncompassingEncounter.Id();
            medicalCaseId.setRoot(recipe.getProviderOrganization().getOid() + ".100.1.1.15");
            medicalCaseId.setExtension(recipe.getMedicalCareCase().getMisId().toString());
            clinicalDocument.getComponentOf().getEncompassingEncounter().getId().add(medicalCaseId);

            // Идентификатор случая оказания медицинской помощи (номер медицинской карты)
            var medicalCardNumber = new ClinicalDocument.ComponentOf.EncompassingEncounter.Id();
            medicalCardNumber.setRoot(recipe.getProviderOrganization().getOid() + ".100.1.1.16");
            medicalCardNumber.setExtension(recipe.getMedicalCareCase().getMedicalCardNumber());
            clinicalDocument.getComponentOf().getEncompassingEncounter().getId().add(medicalCardNumber);

            // Даты начала и окончания случая
            clinicalDocument.getComponentOf().getEncompassingEncounter().getEffectiveTime().setLow(new ClinicalDocument.ComponentOf.EncompassingEncounter.EffectiveTime.Low());
            clinicalDocument.getComponentOf().getEncompassingEncounter().getEffectiveTime().getLow().setValue(recipe.getMedicalCareCase().getBeginDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
            clinicalDocument.getComponentOf().getEncompassingEncounter().getEffectiveTime().setHigh(new ClinicalDocument.ComponentOf.EncompassingEncounter.EffectiveTime.High());
            clinicalDocument.getComponentOf().getEncompassingEncounter().getEffectiveTime().getHigh().setValue(recipe.getMedicalCareCase().getEndDate().format(DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
        }

        // Тело документа
        clinicalDocument.setComponent(new ClinicalDocument.Component());
        clinicalDocument.getComponent().setStructuredBody(new ClinicalDocument.Component.StructuredBody());

        // Секция сведения о документе
        var documentSection = new ClinicalDocument.Component.StructuredBody.ComponentSection();
        documentSection.setSection(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section());

        // Код секции
        documentSection.getSection().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Code());
        documentSection.getSection().getCode().setCode(DOC_INFO_CODE);
        documentSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
        documentSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
        documentSection.getSection().getCode().setCodeSystemVersion("1.7");
        documentSection.getSection().getCode().setDisplayName(DOC_INFO_NAME);

        documentSection.getSection().setTitle(EL_RECIPE_TITLE);

        // Наполнение секции
        documentSection.getSection().setText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text());
        documentSection.getSection().getText().setTable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table());
        documentSection.getSection().getText().getTable().setTbody(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody());

        // Приоритет исполнения
        if (recipe.getPriority() != null) {
            documentSection.getSection().getText().getTable().getTbody().getTr().add(
                    createTr(RECIPE_PRIORITY, "DOC_1", recipe.getPriority())
            );
            documentSection.getSection().getEntry().add(
                    createEntry(
                            6000,
                            RECIPE_PRIORITY,
                            DOC_1,
                            "1",
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
                            "CD"
                    )
            );
        }

        // Серия рецепта
        documentSection.getSection().getText().getTable().getTbody().getTr().add(
                createTr(RECIPE_SERIES_NAME, "DOC_2", recipe.getSeries())
        );
        documentSection.getSection().getEntry().add(
                createEntry(
                        6001,
                        RECIPE_SERIES_NAME,
                        "#DOC_2",
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
                        "ST"
                )
        );

        // Номер рецепта
        documentSection.getSection().getText().getTable().getTbody().getTr().add(
                createTr(RECIPE_NUMBER_NAME, "DOC_3", recipe.getNumber())
        );
        documentSection.getSection().getEntry().add(
                createEntry(
                        6002,
                        RECIPE_NUMBER_NAME,
                        "#DOC_3",
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
                        "ST"
                )
        );

        // Протокол врачебной комиссии
        if (recipe.getMedicalCommissionProtocol() != null) {
            documentSection.getSection().getText().getTable().getTbody().getTr().add(
                    createTr(
                            PROTOCOL_NAME,
                            "DOC_4",
                            recipe.getMedicalCommissionProtocol().getNumber() + " от " + recipe.getMedicalCommissionProtocol().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                    )
            );
            documentSection.getSection().getEntry().add(
                    createEntry(
                            4059,
                            PROTOCOL_NAME,
                            "#DOC_4",
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
                            "ST"
                    )
            );
        }

        // Срок действия рецепта
        documentSection.getSection().getText().getTable().getTbody().getTr().add(
                createTr(RECIPE_VALIDITY, "DOC_7", recipe.getValidity())
        );
        documentSection.getSection().getEntry().add(
                createEntry(
                        6004,
                        RECIPE_VALIDITY,
                        "#DOC_7",
                        "1",
                        RECIPE_VALIDITY_CODE_SYSTEM,
                        RECIPE_VALIDITY,
                        "1.2",
                        recipe.getValidity(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        "CD"
                )
        );

        // Дата окончания действия рецепта
        if (recipe.getEndDate() != null) {
            documentSection.getSection().getText().getTable().getTbody().getTr().add(
                    createTr(
                            END_DATE_DISPLAY_NAME, "DOC_8",
                            recipe.getEndDate().format(DateTimeFormatter.ofPattern(RUS_DATE_FORMAT))
                    )
            );
            documentSection.getSection().getEntry().add(
                    createEntry(
                            6005,
                            END_DATE_DISPLAY_NAME,
                            "#DOC_8",
                            null,
                            null,
                            null,
                            null,
                            null,
                            recipe.getEndDate().format(DateTimeFormatter.ofPattern(IEMK_DATE_FORMAT)),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "TS"
                    )
            );
        }

        // По специальному назначению (Отметка)
        documentSection.getSection().getText().getTable().getTbody().getTr().add(
                createTr(SPECIAL_NAME, "DOC_9", recipe.isSpecial() ? "имеется" : "отсутствует")
        );
        documentSection.getSection().getEntry().add(
                createEntry(
                        6006,
                        SPECIAL_NAME,
                        "#DOC_9",
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
                        "BL"
                )
        );

        // Код заболевания по МКБ-10
        if (recipe.getMkb() != null) {
            documentSection.getSection().getText().getTable().getTbody().getTr().add(
                    createTr("Код заболевания по МКБ-10", "DOC_10", recipe.getMkb().getCode())
            );
            documentSection.getSection().getEntry().add(
                    createEntry(
                            809,
                            "Шифр по МКБ-10",
                            "#DOC_9",
                            recipe.getMkb().getCode(),
                            "1.2.643.5.1.13.13.11.1005",
                            "Международная статистическая классификация болезней и проблем, связанных со здоровьем (10-й пересмотр)",
                            "2.10",
                            recipe.getMkb().getName(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "CD"
                    )
            );
        }

        // Наличие хронических заболеваний
        documentSection.getSection().getEntry().add(
                createEntry(
                        11001,
                        "Наличие хронических заболеваний",
                        null,
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
                        "BL"
                )
        );

        clinicalDocument.getComponent().getStructuredBody().getComponent().add(documentSection);

        if (recipe.getBenefit() != null && recipe.getFinancing() != null) {
            // Секция коды льгот (с кодированными элементами)
            var benefitSection = new ClinicalDocument.Component.StructuredBody.ComponentSection();
            benefitSection.setSection(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section());

            // Код секции
            benefitSection.getSection().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Code());
            benefitSection.getSection().getCode().setCode("BENEFITS");
            benefitSection.getSection().getCode().setCodeSystem(SECTION_CODE_SYSTEM);
            benefitSection.getSection().getCode().setCodeSystemName(DOC_CODE_SYSTEM_NAME);
            benefitSection.getSection().getCode().setCodeSystemVersion("1.7");
            benefitSection.getSection().getCode().setDisplayName("Льготы");

            benefitSection.getSection().setTitle("Коды льгот");

            // Наполнение секции
            benefitSection.getSection().setText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text());
            benefitSection.getSection().getText().setTable(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table());
            benefitSection.getSection().getText().getTable().setTbody(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody());

            // Льготная категория
            benefitSection.getSection().getText().getTable().getTbody().getTr().add(
                    createTr(BENEFIT_NAME, "BEN_1", recipe.getBenefit().getName())
            );
            benefitSection.getSection().getEntry().add(
                    createEntry(
                            811,
                            BENEFIT_NAME,
                            "#BEN_1",
                            recipe.getBenefit().getCode(),
                            BENEFIT_CODE_SYSTEM,
                            BENEFIT_CODE_SYSTEM_NAME,
                            "6.3",
                            recipe.getBenefit().getName(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "CD"
                    )
            );

            // Источник финансирования
            if (recipe.getFinancing() != null) {
                benefitSection.getSection().getText().getTable().getTbody().getTr().add(
                        createTr(FINANCING_NAME, "BEN_2", recipe.getFinancing().getName())
                );
                benefitSection.getSection().getEntry().add(
                        createEntry(
                                6008,
                                FINANCING_NAME,
                                "#BEN_2",
                                recipe.getFinancing().getCode(),
                                BENEFIT_CODE_SYSTEM,
                                BENEFIT_CODE_SYSTEM_NAME,
                                "6.3",
                                recipe.getFinancing().getName(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                "CD"
                        )
                );
            }

            // Размер льготы код
            if (recipe.getBenefit() != null) {
                benefitSection.getSection().getEntry().add(
                        createEntry(
                                6009,
                                "Размер льготы (код)",
                                null,
                                "106",
                                "1.2.643.5.1.13.13.99.2.605",
                                "Виды предоставляемых льгот",
                                "2.1",
                                recipe.getBenefit().getAmount(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                "CD"
                        )
                );

                // Размер льготы %
                benefitSection.getSection().getText().getTable().getTbody().getTr().add(
                        createTr("Размер льготы", "BEN_3", recipe.getBenefit().getPercent() + "%")
                );
                benefitSection.getSection().getEntry().add(
                        createEntry(
                                6010,
                                "Размер льготы (значение в процентах)",
                                "#BEN_3",
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
                                "PQ"
                        )
                );
            }
            clinicalDocument.getComponent().getStructuredBody().getComponent().add(benefitSection);
        }
    }

    private ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr createTr(String name, String contentId, String content) {
        var tr = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr();
        var tdTitle = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr.Td();
        tdTitle.getContent().add(name);
        tr.getTd().add(tdTitle);
        var tdContent = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr.Td();
        tdContent.setID(contentId);
        tdContent.getContent().add(content);
        tr.getTd().add(tdContent);
        return tr;
    }

    private ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr createMedProdTr(String title, String content) {
        var tr = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr();
        tr.setTh(title);
        var tdContent = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr.Td();
        tdContent.getContent().add(content);
        tr.getTd().add(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr.Td());
        return tr;
    }

    private ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr createRecipeSectionTr() {
        var tr = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr();
        tr.setID("med");
        return tr;
    }

    private ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr.Td createRecipeSectionTd(String tdId, String content) {
        var td = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr.Td();
        td.setID(tdId);
        td.getContent().add(content);
        return td;
    }

    private ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry createEntry(
            int code,
            String codeDisplayName,
            String reference,
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
            String valueType
    ) {
        var entry = new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry();
        entry.setObservation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Observation());
        entry.getObservation().setClassCode("OBS");
        entry.getObservation().setMoodCode("EVN");
        entry.getObservation().setCode(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Observation.Code());
        entry.getObservation().getCode().setCode(code);
        entry.getObservation().getCode().setCodeSystem(IEMK_CODE_SYSTEM);
        entry.getObservation().getCode().setCodeSystemName(IEMK_CODE_SYSTEM_NAME);
        entry.getObservation().getCode().setCodeSystemVersion("1.31");
        entry.getObservation().getCode().setDisplayName(codeDisplayName);
        if (reference != null) {
            entry.getObservation().getCode().setOriginalText(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Observation.Code.OriginalText());
            entry.getObservation().getCode().getOriginalText().setReference(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Observation.Code.OriginalText.Reference());
            entry.getObservation().getCode().getOriginalText().getReference().setValue(reference);
        }
        if (valueCode != null && valueCodeSystem != null && valueCodeSystemName != null && valueCodeSystemVersion != null && valueDisplayName != null) {
            entry.getObservation().setValue(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Observation.Value());
            entry.getObservation().getValue().setType(valueType);
            entry.getObservation().getValue().setCode(valueCode);
            entry.getObservation().getValue().setCodeSystem(valueCodeSystem);
            entry.getObservation().getValue().setCodeSystemName(valueCodeSystemName);
            entry.getObservation().getValue().setCodeSystemVersion(valueCodeSystemVersion);
            entry.getObservation().getValue().setDisplayName(valueDisplayName);
        }
        if (value != null) {
            entry.getObservation().setValue(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Observation.Value());
            entry.getObservation().getValue().setType(valueType);
            entry.getObservation().getValue().setValue(value);
            entry.getObservation().getValue().setUnit(unit);
            if (translationCode != null && translationDisplayName != null) {
                entry.getObservation().getValue().setTranslation(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Observation.Value.Translation());
                entry.getObservation().getValue().getTranslation().setCode((short) Integer.parseInt(translationCode));
                entry.getObservation().getValue().getTranslation().setCodeSystem(TRANSLATION_CODE_SYSTEM);
                entry.getObservation().getValue().getTranslation().setCodeSystemVersion("2.6");
                entry.getObservation().getValue().getTranslation().setCodeSystemName(TRANSLATION_CODE_SYSTEM_NAME);
                entry.getObservation().getValue().getTranslation().setDisplayName(translationDisplayName);
                entry.getObservation().getValue().getTranslation().setValue((short) Integer.parseInt(translationValue));
            }
        }
        if (valueContent != null) {
            entry.getObservation().setValue(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Observation.Value());
            entry.getObservation().getValue().setType(valueType);
            entry.getObservation().getValue().getContent().add(valueContent);
        }
        if (effectiveTime != null) {
            entry.getObservation().setEffectiveTime(new ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Entry.Observation.EffectiveTime());
            entry.getObservation().getEffectiveTime().setValue(effectiveTime);
        }
        return entry;
    }

    private POCDMT000040Entry createEntryNewEdition(
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

    public String createIemkPreferentialRecipeDocumentNewEdition(FullRecipe fullRecipe, int version) {
        if (fullRecipe.getDrug() != null) {
            return createIemkDrugDocumentNewEdition(fullRecipe, version);
        } else if (fullRecipe.getMedicalProduct() != null) {
            return createIemkMedicalProductDocumentNewEdition(fullRecipe, version);
        } else {
            return createIemkHealthFoodDocumentNewEdition(fullRecipe, version);
        }
    }

    private String createIemkDrugDocumentNewEdition(FullRecipe recipe, int version) {
        var clinicalDocument = createIemkClinicalDocumentCommonDataNewEdition(recipe, version, true);

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

    private String createIemkMedicalProductDocumentNewEdition(FullRecipe recipe, int version) {
        var clinicalDocument = createIemkClinicalDocumentCommonDataNewEdition(recipe, version, true);

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

    private String createIemkHealthFoodDocumentNewEdition(FullRecipe recipe, int version) {
        var clinicalDocument = createIemkClinicalDocumentCommonDataNewEdition(recipe, version, true);

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

    private POCDMT000040ClinicalDocument createIemkClinicalDocumentCommonDataNewEdition(Recipe recipe, int version, boolean preferential) {
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
                        createTrNewEdition(RECIPE_PRIORITY, recipe.getPriority())
                );
                documentSection.getSection().getEntry().add(
                        createEntryNewEdition(
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
                    createTrNewEdition(RECIPE_SERIES_NAME, recipe.getSeries())
            );
            documentSection.getSection().getEntry().add(
                    createEntryNewEdition(
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
                    createTrNewEdition(RECIPE_NUMBER_NAME, recipe.getNumber())
            );
            documentSection.getSection().getEntry().add(
                    createEntryNewEdition(
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
                        createTrNewEdition(
                                PROTOCOL_NAME,
                                recipe.getMedicalCommissionProtocol().getNumber() + " от " + recipe.getMedicalCommissionProtocol().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                        )
                );
                documentSection.getSection().getEntry().add(
                        createEntryNewEdition(
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
                createTrNewEdition(RECIPE_VALIDITY, recipe.getValidity())
        );
        documentSection.getSection().getEntry().add(
                createEntryNewEdition(
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
                    createTrNewEdition(
                            END_DATE_DISPLAY_NAME,
                            recipe.getEndDate().format(DateTimeFormatter.ofPattern(RUS_DATE_FORMAT))
                    )
            );
        }

        // По специальному назначению (Отметка)
        strucDocTbody.getTr().add(
                createTrNewEdition(SPECIAL_NAME, recipe.isSpecial() ? "имеется" : "отсутствует")
        );
        documentSection.getSection().getEntry().add(
                createEntryNewEdition(
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
                    createTrNewEdition("Код заболевания по МКБ-10", recipe.getMkb().getCode())
            );
            documentSection.getSection().getEntry().add(
                    createEntryNewEdition(
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
                    createEntryNewEdition(
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
                    createTrNewEdition(BENEFIT_NAME, recipe.getBenefit().getName())
            );
            benefitSection.getSection().getEntry().add(
                    createEntryNewEdition(
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
                    createTrNewEdition(FINANCING_NAME, recipe.getFinancing().getName())
            );

            // Размер льготы код
            benefitSection.getSection().getEntry().add(
                    createEntryNewEdition(
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
                    createTrNewEdition("Размер льготы", recipe.getBenefit().getPercent() + "%")
            );
            benefitSection.getSection().getEntry().add(
                    createEntryNewEdition(
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

    private StrucDocTr createTrNewEdition(String name, String content) {
        var tr = new StrucDocTr();
        var tdTitle = new StrucDocTd();
        tdTitle.getContent().add(createStrucDocContentNewEdition(name));
        tr.getThOrTd().add(tdTitle);
        var tdContent = new StrucDocTd();
        tdContent.getContent().add(createStrucDocContentNewEdition(content));
        tr.getThOrTd().add(tdContent);
        return tr;
    }

    private JAXBElement createStrucDocContentNewEdition(String content) {
        var strucDocContent = new StrucDocContent();
        strucDocContent.getContent().add(content);
        return new JAXBElement(new QName(URN_HL_7_ORG_V_3, "content"), StrucDocContent.class, strucDocContent);
    }

    public DrugRecipe createDrugRecipe(ClinicalDocument clinicalDocument) {
        var recipe = createRecipe(clinicalDocument);
        var drugRecipe = new DrugRecipe(recipe);
        drugRecipe.setDrug(new Drug());
        var medDispense = clinicalDocument.getComponent().getStructuredBody().getComponent().stream().filter(c -> c.getSection().getCode().getCode().equals("MEDDISPENSE")).findAny().orElse(null);
        if (medDispense != null) {
            var entry = medDispense.getSection().getEntry().stream().filter(e -> e.getSupply() != null).findAny().orElse(null);
            if (entry != null) {
                drugRecipe.getDrug().setCodeMnn(entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().getCode());
                drugRecipe.getDrug().setName(entry.getSupply().getProduct().getManufacturedProduct().getManufacturedMaterial().getCode().getDisplayName());
                drugRecipe.getDrug().setPackAmount((int) entry.getSupply().getQuantity().getValue());
                drugRecipe.getDrug().setPrice(new BigDecimal(entry.getSupply().getEntryRelationship().getObservation().getValue().getValue()));
            }
        }
        return drugRecipe;
    }

    private Recipe createRecipe(ClinicalDocument clinicalDocument) {
        var recipe = new Recipe();
        recipe.setMisId(clinicalDocument.getId().getExtension());
        recipe.setName(clinicalDocument.getTitle());

        // серия/номер
        var docInfo = clinicalDocument.getComponent().getStructuredBody().getComponent()
                .stream()
                .filter(c -> c.getSection().getCode().getCode().equals(DOC_INFO_CODE))
                .findAny()
                .orElse(null);
        if (docInfo != null) {
            var contentList = docInfo.getSection().getText().getTable().getTbody().getTr()
                    .stream()
                    .flatMap(tr -> tr.getTd().stream())
                    .flatMap(td -> td.getContent().stream())
                    .collect(Collectors.toList());
            for (var contentSerializable : contentList) {
                var contentElement = new JAXBElement(new QName("content"), ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr.Td.Content.class, contentSerializable);
                if (contentElement.getValue() instanceof JAXBElement) {
                    var content = (JAXBElement<ClinicalDocument.Component.StructuredBody.ComponentSection.Section.Text.Table.Tbody.Tr.Td.Content>) contentElement.getValue();
                    if (content.getValue().getID() != null && content.getValue().getID().equals("DOC_2")) {
                        recipe.setSeries(content.getValue().getValue());
                    } else {
                        recipe.setNumber(content.getValue().getValue());
                    }
                }
            }
        }

        if (clinicalDocument.getEffectiveTime().getValue().length() < 9) {
            recipe.setCreateDate(ZonedDateTime.from(LocalDate.parse(clinicalDocument.getEffectiveTime().getValue(), DateTimeFormatter.ofPattern(IEMK_DATE_FORMAT)).atStartOfDay(ZoneId.of("+03:00"))));
        } else {
            recipe.setCreateDate(ZonedDateTime.parse(clinicalDocument.getEffectiveTime().getValue(), DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
        }
        recipe.setVersionId(clinicalDocument.getSetId().getExtension());
        recipe.setVersionNumber((int) clinicalDocument.getVersionNumber().getValue());
        recipe.setPatient(new Patient());

        // SNILS
        clinicalDocument.getRecordTarget().getPatientRole().getId()
                .stream()
                .filter(o -> o.getRoot().equals(SNILS_ROOT))
                .findAny()
                .ifPresent(patientSnils -> recipe.getPatient().setSnils(patientSnils.getExtension()));

        // misId
        clinicalDocument.getRecordTarget().getPatientRole().getId().
                stream()
                .filter(o -> !o.getRoot().equals(SNILS_ROOT))
                .findAny()
                .ifPresent(patientMisId -> recipe.getPatient().setMisId(patientMisId.getExtension()));

        if (clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc() != null) {
            recipe.getPatient().setIdentityDoc(new su.medsoft.rir.recipe.dto.rir.IdentityDoc());
            recipe.getPatient().getIdentityDoc().setSeries(clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getSeries().getValue());
            recipe.getPatient().getIdentityDoc().setNumber(clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getNumber().getValue());
            recipe.getPatient().getIdentityDoc().setIssueOrgCode(clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIssueOrgCode().getValue());
            recipe.getPatient().getIdentityDoc().setIssueOrgName(clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIssueOrgName().getValue());
            recipe.getPatient().getIdentityDoc().setIssueDate(LocalDate.parse(String.valueOf(clinicalDocument.getRecordTarget().getPatientRole().getIdentityDoc().getIssueDate().getValue()), DateTimeFormatter.ofPattern(IEMK_DATE_FORMAT)));
        }

        if (!clinicalDocument.getRecordTarget().getPatientRole().getAddr().isEmpty()) {
            var address = clinicalDocument.getRecordTarget().getPatientRole().getAddr().get(0);
            recipe.getPatient().setAddress(new su.medsoft.rir.recipe.dto.rir.Address());
            recipe.getPatient().getAddress().setRegionCode(address.getState().toString());
            recipe.getPatient().getAddress().setFullAddress(address.getStreetAddressLine());
            if (address.getAddress() != null) {
                recipe.getPatient().getAddress().setHouseGuid(address.getAddress().getHOUSEGUID());
                recipe.getPatient().getAddress().setAoGuid(address.getAddress().getAOGUID());
            }
        }
        recipe.getPatient().setLastName(clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().getFamily());
        if (!clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().getGiven().isEmpty()) {
            recipe.getPatient().setFirstName(clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().getGiven().get(0));
            if (clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().getGiven().size() > 1) {
                recipe.getPatient().setMiddleName(clinicalDocument.getRecordTarget().getPatientRole().getPatient().getName().getGiven().get(1));
            }
        }
        if (clinicalDocument.getRecordTarget().getPatientRole().getPatient().getBirthTime() != null) {
            recipe.getPatient().setBirthDate(LocalDate.parse(String.valueOf(clinicalDocument.getRecordTarget().getPatientRole().getPatient().getBirthTime().getValue()), DateTimeFormatter.ofPattern(IEMK_DATE_FORMAT)));
        }
        if (clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode() != null) {
            recipe.getPatient().setGender(clinicalDocument.getRecordTarget().getPatientRole().getPatient().getAdministrativeGenderCode().getCode() == 1 ? "М" : "Ж");
        }

        recipe.setProviderOrganization(new ProviderOrganization());
        recipe.getProviderOrganization().setOid(clinicalDocument.getId().getRoot().substring(0, 30));
        if (clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getOgrn() != null) {
            recipe.getProviderOrganization().setOgrn(clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getOgrn().toString());
        }
        recipe.getProviderOrganization().setName(clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getName());
        if (clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr() != null) {
            var address = clinicalDocument.getRecordTarget().getPatientRole().getProviderOrganization().getAddr();
            recipe.getProviderOrganization().setAddress(new su.medsoft.rir.recipe.dto.rir.Address());
            recipe.getProviderOrganization().getAddress().setRegionCode(String.valueOf(address.getState()));
            recipe.getProviderOrganization().getAddress().setFullAddress(address.getStreetAddressLine());
            if (address.getAddress() != null) {
                recipe.getProviderOrganization().getAddress().setHouseGuid(address.getAddress().getHOUSEGUID());
                recipe.getProviderOrganization().getAddress().setAoGuid(address.getAddress().getAOGUID());
            }
        }
        recipe.setSignatureDate(ZonedDateTime.parse(clinicalDocument.getAuthor().getTime().getValue(), DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
        recipe.setAuthor(new Author());

        // SNILS
        clinicalDocument.getAuthor().getAssignedAuthor().getId()
                .stream()
                .filter(o -> o.getRoot().equals(SNILS_ROOT))
                .findAny()
                .ifPresent(authorSnils -> recipe.getAuthor().setSnils(authorSnils.getExtension()));

        // misId
        clinicalDocument.getAuthor().getAssignedAuthor().getId().
                stream()
                .filter(o -> !o.getRoot().equals(SNILS_ROOT))
                .findAny()
                .ifPresent(authorMisId -> recipe.getAuthor().setMisId(authorMisId.getExtension()));

        if (!clinicalDocument.getAuthor().getAssignedAuthor().getTelecom().isEmpty()) {
            recipe.getAuthor().setPhone(clinicalDocument.getAuthor().getAssignedAuthor().getTelecom().get(0).getValue());
        }

        recipe.getAuthor().setPost(new Post());
        recipe.getAuthor().getPost().setCode(clinicalDocument.getAuthor().getAssignedAuthor().getCode().getCode());
        recipe.getAuthor().getPost().setName(clinicalDocument.getAuthor().getAssignedAuthor().getCode().getDisplayName());

        recipe.getAuthor().setLastName(clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().getFamily());
        if (!clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().getGiven().isEmpty()) {
            recipe.getAuthor().setFirstName(clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().getGiven().get(0));
            if (clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().getGiven().size() > 1) {
                recipe.getAuthor().setMiddleName(clinicalDocument.getAuthor().getAssignedAuthor().getAssignedPerson().getName().getGiven().get(1));
            }
        }

        recipe.getAuthor().setRepresentedOrganisation(new Organisation());
        recipe.getAuthor().getRepresentedOrganisation().setOid(clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getId().getRoot());
        recipe.getAuthor().getRepresentedOrganisation().setName(clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getName());
        if (clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr() != null) {
            var address = clinicalDocument.getAuthor().getAssignedAuthor().getRepresentedOrganization().getAddr();
            recipe.getAuthor().getRepresentedOrganisation().setAddress(new su.medsoft.rir.recipe.dto.rir.Address());
            recipe.getAuthor().getRepresentedOrganisation().getAddress().setRegionCode(String.valueOf(address.getState()));
            recipe.getAuthor().getRepresentedOrganisation().getAddress().setFullAddress(address.getStreetAddressLine());
            if (address.getAddress() != null) {
                recipe.getAuthor().getRepresentedOrganisation().getAddress().setHouseGuid(address.getAddress().getHOUSEGUID());
                recipe.getAuthor().getRepresentedOrganisation().getAddress().setAoGuid(address.getAddress().getAOGUID());
            }
        }

        recipe.setCustodianOrganisation(new Organisation());
        recipe.getCustodianOrganisation().setOid(clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getId().getRoot());
        recipe.getCustodianOrganisation().setName(clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getName());
        if (clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr() != null) {
            var address = clinicalDocument.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization().getAddr();
            recipe.getCustodianOrganisation().setAddress(new su.medsoft.rir.recipe.dto.rir.Address());
            recipe.getCustodianOrganisation().getAddress().setRegionCode(String.valueOf(address.getState()));
            recipe.getCustodianOrganisation().getAddress().setFullAddress(address.getStreetAddressLine());
            if (address.getAddress() != null) {
                recipe.getCustodianOrganisation().getAddress().setHouseGuid(address.getAddress().getHOUSEGUID());
                recipe.getCustodianOrganisation().getAddress().setAoGuid(address.getAddress().getAOGUID());
            }
        }

        recipe.setLegalAuthenticatorSignatureDate(ZonedDateTime.parse(clinicalDocument.getLegalAuthenticator().getTime().getValue(), DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));

        recipe.setLegalAuthenticator(new Staff());
        // SNILS
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getId()
                .stream()
                .filter(o -> o.getRoot().equals(SNILS_ROOT))
                .findAny()
                .ifPresent(authorSnils -> recipe.getLegalAuthenticator().setSnils(authorSnils.getExtension()));

        // misId
        clinicalDocument.getLegalAuthenticator().getAssignedEntity().getId().
                stream()
                .filter(o -> !o.getRoot().equals(SNILS_ROOT))
                .findAny()
                .ifPresent(authorMisId -> recipe.getLegalAuthenticator().setMisId(authorMisId.getExtension()));

        recipe.getLegalAuthenticator().setPost(new Post());
        recipe.getLegalAuthenticator().getPost().setCode((int) clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().getCode());
        recipe.getAuthor().getPost().setName(clinicalDocument.getLegalAuthenticator().getAssignedEntity().getCode().getDisplayName());

        recipe.getLegalAuthenticator().setLastName(clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().getFamily());
        if (!clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().getGiven().isEmpty()) {
            recipe.getLegalAuthenticator().setFirstName(clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().getGiven().get(0));
            if (clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().getGiven().size() > 1) {
                recipe.getLegalAuthenticator().setMiddleName(clinicalDocument.getLegalAuthenticator().getAssignedEntity().getAssignedPerson().getName().getGiven().get(1));
            }
        }

        recipe.getLegalAuthenticator().setRepresentedOrganisation(new Organisation());
        recipe.getLegalAuthenticator().getRepresentedOrganisation().setOid(clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getId().getRoot());
        recipe.getLegalAuthenticator().getRepresentedOrganisation().setName(clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getName());
        if (clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr() != null) {
            var address = clinicalDocument.getLegalAuthenticator().getAssignedEntity().getRepresentedOrganization().getAddr();
            recipe.getLegalAuthenticator().getRepresentedOrganisation().setAddress(new su.medsoft.rir.recipe.dto.rir.Address());
            recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().setRegionCode(String.valueOf(address.getState()));
            recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().setFullAddress(address.getStreetAddressLine());
            if (address.getAddress() != null) {
                recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().setHouseGuid(address.getAddress().getHOUSEGUID());
                recipe.getLegalAuthenticator().getRepresentedOrganisation().getAddress().setAoGuid(address.getAddress().getAOGUID());
            }
        }

        if (clinicalDocument.getParticipant() != null && clinicalDocument.getParticipant().getAssociatedEntity() != null) {
            recipe.getPatient().setInsurance(new Insurance());
            recipe.getPatient().getInsurance().setNumber(clinicalDocument.getParticipant().getAssociatedEntity().getId().getExtension().toString());
            recipe.getPatient().getInsurance().setInsuranceOrganisation(new InsuranceOrganisation());
            recipe.getPatient().getInsurance().getInsuranceOrganisation().setNsiId((int) clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getId().getExtension());
            recipe.getPatient().getInsurance().getInsuranceOrganisation().setName(clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getName());
            if (clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getTelecom() != null) {
                recipe.getPatient().getInsurance().getInsuranceOrganisation().setPhone(clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getTelecom().getValue());
            }
            if (clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getAddr() != null) {
                var address = clinicalDocument.getParticipant().getAssociatedEntity().getScopingOrganization().getAddr();
                recipe.getPatient().getInsurance().getInsuranceOrganisation().setAddress(new su.medsoft.rir.recipe.dto.rir.Address());
                recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress().setRegionCode(String.valueOf(address.getState()));
                recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress().setFullAddress(address.getStreetAddressLine());
                if (address.getAddress() != null) {
                    recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress().setHouseGuid(address.getAddress().getHOUSEGUID());
                    recipe.getPatient().getInsurance().getInsuranceOrganisation().getAddress().setAoGuid(address.getAddress().getAOGUID());
                }
            }
        }

        if (clinicalDocument.getComponentOf() != null && clinicalDocument.getComponentOf().getEncompassingEncounter() != null) {
            recipe.setMedicalCareCase(new MedicalCareCase());

            // misId
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getId().
                    stream()
                    .filter(o -> o.getRoot().endsWith("15"))
                    .findAny()
                    .ifPresent(misId -> recipe.getLegalAuthenticator().setMisId(misId.getExtension()));

            // cardNumber
            clinicalDocument.getLegalAuthenticator().getAssignedEntity().getId()
                    .stream()
                    .filter(o -> o.getRoot().endsWith("16"))
                    .findAny()
                    .ifPresent(cardNumber -> recipe.getLegalAuthenticator().setSnils(cardNumber.getExtension()));

            recipe.getMedicalCareCase().setBeginDate(ZonedDateTime.parse(clinicalDocument.getComponentOf().getEncompassingEncounter().getEffectiveTime().getLow().getValue(), DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
            recipe.getMedicalCareCase().setEndDate(ZonedDateTime.parse(clinicalDocument.getComponentOf().getEncompassingEncounter().getEffectiveTime().getHigh().getValue(), DateTimeFormatter.ofPattern(IEMK_ZONED_DATE_TIME_FORMAT)));
        }
        return recipe;
    }
}
