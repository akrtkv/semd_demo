Тестовый интерфейс доступен по адресу http://localhost:8080/semd_demo/swagger-ui.html

JSON для формирования СЭМД на лекарственный препарат

```
{
  "id": null,
  "misId": 31202,
  "name": "Льготный рецепт на лекарственный препарат, изделие медицинского назначения и специализированный продукт лечебного питания",
  "createDate": "2021-11-25T00:00:00+03:00",
  "versionId": 1,
  "versionNumber": 1,
  "patient": {
    "id": null,
    "misId": 515954,
    "lastName": "Кортнева",
    "firstName": "Тамара",
    "middleName": "Николаевна",
    "birthDate": "1963-05-24",
    "gender": "Ж",
    "snils": "07794923429",
    "insurance": {
      "id": null,
      "number": "4854630875000179",
      "insuranceOrganisation": {
        "id": null,
        "nsiId": 0,
        "name": "АКЦИОНЕРНОЕ ОБЩЕСТВО \"СТРАХОВАЯ КОМПАНИЯ \"СОГАЗ-МЕД\"",
        "phone": "88001008102",
        "address": {
          "id": null,
          "fullAddress": "107045, г.Москва, пер.Уланский, д.26, помещение 3.01",
          "regionCode": "77",
          "regionName": "Московская Область",
          "aoGuid": "8267143d-64b3-4af7-8dfd-6cd283a60f86",
          "houseGuid": "72cf2ec0-3bfa-4bdf-a4f3-5f9a5392faf0"
        }
      }
    },
    "identityDoc": null,
    "address": {
      "id": null,
      "fullAddress": "Липецкая область",
      "regionCode": "77",
      "regionName": "Липецкая область",
      "aoGuid": null,
      "houseGuid": null
    }
  },
  "providerOrganization": {
    "id": null,
    "oid": "1.2.643.5.1.13.13.12.2.48.4497",
    "name": "Государственное учреждение здравоохранения \"Липецкая городская поликлиника № 4\"",
    "address": {
      "id": null,
      "fullAddress": "г. Липецк, ул. Гагарина, д. 139",
      "regionCode": "77",
      "regionName": "Московская Область",
      "aoGuid": "6442d40d-5c01-42f2-80e9-d957d028b2c5",
      "houseGuid": "29712bd6-cc21-4c61-9927-1a61f7a64665"
    },
    "ogrn": "1024840851518",
    "phone": "+79508051650"
  },
  "signatureDate": "2021-11-25T10:18:16+03:00",
  "author": {
    "id": null,
    "misId": 1292,
    "lastName": "Серикова",
    "firstName": "Алеся",
    "middleName": "Александровна",
    "snils": "11612759843",
    "post": {
      "code": 125,
      "name": "врач-эндокринолог"
    },
    "representedOrganisation": {
      "id": null,
      "oid": "1.2.643.5.1.13.13.12.2.48.4497",
      "name": "Государственное учреждение здравоохранения \"Липецкая городская поликлиника № 4\"",
      "address": {
        "id": null,
        "fullAddress": "г. Липецк, ул. Гагарина, д. 139",
        "regionCode": "77",
        "regionName": "Московская Область",
        "aoGuid": "6442d40d-5c01-42f2-80e9-d957d028b2c5",
        "houseGuid": "29712bd6-cc21-4c61-9927-1a61f7a64665"
      }
    },
    "phone": "+79205768518"
  },
  "custodianOrganisation": {
    "id": null,
    "oid": "1.2.643.5.1.13.13.12.2.48.4497",
    "name": "Государственное учреждение здравоохранения \"Липецкая городская поликлиника № 4\"",
    "address": {
      "id": null,
      "fullAddress": "г. Липецк, ул. Гагарина, д. 139",
      "regionCode": "77",
      "regionName": "Московская Область",
      "aoGuid": "6442d40d-5c01-42f2-80e9-d957d028b2c5",
      "houseGuid": "29712bd6-cc21-4c61-9927-1a61f7a64665"
    }
  },
  "legalAuthenticatorSignatureDate": "2021-11-25T10:18:16+03:00",
  "legalAuthenticator": {
    "id": null,
    "misId": 1292,
    "lastName": "Серикова",
    "firstName": "Алеся",
    "middleName": "Александровна",
    "snils": "11612759843",
    "post": {
      "code": 125,
      "name": "врач-эндокринолог"
    },
    "representedOrganisation": {
      "id": null,
      "oid": "1.2.643.5.1.13.13.12.2.48.4497",
      "name": "Государственное учреждение здравоохранения \"Липецкая городская поликлиника № 4\"",
      "address": {
        "id": null,
        "fullAddress": "г. Липецк, ул. Гагарина, д. 139",
        "regionCode": "77",
        "regionName": "Московская Область",
        "aoGuid": "6442d40d-5c01-42f2-80e9-d957d028b2c5",
        "houseGuid": "29712bd6-cc21-4c61-9927-1a61f7a64665"
      }
    }
  },
  "medicalCareCase": {
    "id": null,
    "misId": 31202,
    "medicalCardNumber": "11963",
    "beginDate": "2021-11-25T10:17:39+03:00",
    "endDate": "2021-11-25T10:17:39+03:00"
  },
  "series": "42-21",
  "number": "0004001273",
  "priority": null,
  "medicalCommissionProtocol": {
    "id": null,
    "number": "223",
    "date": "2021-11-25T00:00:00+03:00"
  },
  "validity": "0 месяца",
  "endDate": "2024-11-25",
  "special": false,
  "chronicDisease": false,
  "mkb": {
    "id": null,
    "code": "E11.7",
    "name": "Инсулиннезависимый сахарный диабет с множественными осложнениями"
  },
  "financing": {
    "id": null,
    "code": "2",
    "name": "Бюджет субъекта РФ"
  },
  "benefit": {
    "id": null,
    "typeCode": 1,
    "typeName": "Рецепт на лекарственный препарат",
    "code": "1.00000.0076",
    "name": "Граждане, страдающие диабетом",
    "amount": "100",
    "percent": "100"
  },
  "drug": {
    "id": null,
    "durationDays": 30,
    "codeMnn": "21.20.10.119-000010-1-00030-0000000000000",
    "name": "ТАБЛЕТКИ ПОКРЫТЫЕ ПЛЕНОЧНОЙ ОБОЛОЧКОЙ",
    "routeCode": 2,
    "routeName": "Для приема внутрь",
    "dose": 30
  }
}
```