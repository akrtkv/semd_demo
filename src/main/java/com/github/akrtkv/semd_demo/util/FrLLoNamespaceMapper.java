package com.github.akrtkv.semd_demo.util;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import org.springframework.stereotype.Component;

@Component
public class FrLLoNamespaceMapper extends NamespacePrefixMapper {

    private static final String XSD_PREFIX = "xml";

    private static final String XSD_URI = "http://www.w3.org/2001/XMLSchema";

    private static final String XSI_PREFIX = "xsi";

    private static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if (XSD_URI.equals(namespaceUri)) {
            return XSD_PREFIX;
        } else if (XSI_URI.equals(namespaceUri)) {
            return XSI_PREFIX;
        }
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[]{XSD_URI, XSI_URI};
    }
}
