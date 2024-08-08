package com.github.akrtkv.semd_demo.util;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import org.springframework.stereotype.Component;

@Component
public class IemkNamespaceMapper extends NamespacePrefixMapper {

    private static final String FIAS_PREFIX = "fias";

    private static final String FIAS_URI = "urn:hl7-ru:fias";

    private static final String IDENTITY_PREFIX = "identity";

    private static final String IDENTITY_URI = "urn:hl7-ru:identity";

    private static final String ADDRESS_PREFIX = "address";

    private static final String ADDRESS_URI = "urn:hl7-ru:address";

    private static final String XSI_PREFIX = "xsi";

    private static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if (FIAS_URI.equals(namespaceUri)) {
            return FIAS_PREFIX;
        } else if (IDENTITY_URI.equals(namespaceUri)) {
            return IDENTITY_PREFIX;
        } else if (XSI_URI.equals(namespaceUri)) {
            return XSI_PREFIX;
        } else if (ADDRESS_URI.equals(namespaceUri)) {
            return ADDRESS_PREFIX;
        }
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[]{FIAS_URI, IDENTITY_URI, ADDRESS_URI, XSI_URI};
    }
}
