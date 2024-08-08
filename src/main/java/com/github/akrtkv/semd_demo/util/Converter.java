package su.medsoft.rir.recipe.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import su.medsoft.rir.recipe.dto.egisz.callback.Result;
import su.medsoft.rir.recipe.dto.egisz.callback.Root;
import su.medsoft.rir.recipe.exception.ConverterException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class Converter {

    private final FrLLoNamespaceMapper frLLoNamespaceMapper;

    private final IemkNamespaceMapper iemkNamespaceMapper;

    @Autowired
    public Converter(FrLLoNamespaceMapper frLLoNamespaceMapper, IemkNamespaceMapper iemkNamespaceMapper) {
        this.frLLoNamespaceMapper = frLLoNamespaceMapper;
        this.iemkNamespaceMapper = iemkNamespaceMapper;
    }

    public <T> T deserialize(String xmlString, Class<T> classForSerialize) {
        try {
            var jaxbContext = JAXBContext.newInstance(classForSerialize);
            var jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            var reader = new StringReader(xmlString);
            return classForSerialize.cast(jaxbUnmarshaller.unmarshal(reader));
        } catch (JAXBException e) {
            throw new ConverterException(e);
        }
    }

    public <T> String convertObjectToXmlString(T instanceOfClassForConvert, Class<T> classForConvert, boolean iemk) {
        try {
            var jaxbContext = JAXBContext.newInstance(classForConvert);
            var jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty("jaxb.encoding", "UTF-8");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            if (iemk) {
                jaxbMarshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml-stylesheet type=\"text/xsl\" href=\"Electronic_prescription.xsl\"?>");
                jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", iemkNamespaceMapper);
            } else {
                jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
                jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", frLLoNamespaceMapper);
            }
            var writer = new StringWriter();
            jaxbMarshaller.marshal(instanceOfClassForConvert, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new ConverterException(e);
        }
    }

    public Result serializeCallback(String xmlString) {
        try {
            var jaxbContext = JAXBContext.newInstance(Root.class);
            var jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            var reader = new StringReader(xmlString);
            var root = (Root) jaxbUnmarshaller.unmarshal(reader);
            return root.getResult();
        } catch (JAXBException e) {
            try {
                var jaxbContext = JAXBContext.newInstance(Result.class);
                var jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                var reader = new StringReader(xmlString);
                return (Result) jaxbUnmarshaller.unmarshal(reader);
            } catch (JAXBException ex) {
                throw new ConverterException(ex);
            }
        }
    }

    public String convertToBase64(String xmlDocumentString) {
        return new String(Base64.getEncoder().encode(xmlDocumentString.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public String convertFromBase64(String xmlDocumentString) {
        return new String(Base64.getDecoder().decode(xmlDocumentString.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
