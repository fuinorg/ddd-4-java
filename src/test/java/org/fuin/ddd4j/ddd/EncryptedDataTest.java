/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.ddd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.JaxbUtils.marshal;
import static org.fuin.utils4j.JaxbUtils.unmarshal;

import java.util.Base64;
import java.util.UUID;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.BinaryDataStrategy;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.eclipse.yasson.FieldAccessStrategy;
import org.fuin.utils4j.Utils4J;
import org.junit.Test;

//CHECKSTYLE:OFF
public class EncryptedDataTest {

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.forClass(EncryptedData.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final EncryptedData original = createTestee(UUID.randomUUID());

        // TEST
        final EncryptedData copy = Utils4J.deserialize(Utils4J.serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
    }

    @Test
    public void testConstruction() {

        // PREPARE
        final String keyId = UUID.randomUUID().toString();
        final String keyVersion = "v1";
        final String dataType = "MySecretData";
        final String contentType = "application/json; encoding=UTF-8; version=1";
        final byte[] encryptedData = Base64.getDecoder().decode(
                "d3tHrl9NbYKl1SdDh4AzI5egUZfP2qeCSyNbnLdg/K9feYZSdvoWf+Lz7UJVfntRUNKO8u62OspvcgRRidOAKBFtsCx6U77n0ww7Xh4jbZ27AA3KyDTXB+"
                        + "70PUejSZ2AIDgnxxLhj2IWklOBX7OJO/WxGuT1bsnEUi2GLvE8siCRDa29N2Xm1bvUMpyzdUY6yjfBft9Ju8vTgwarIpTUDrLDYH/P0+iJd3"
                        + "eM+I8gqffe19Jv+OrwuN7D3mLVATDMoZ39vUo0ovw5Gfy1S0U0ErxfcviVqWQRxI2Kp/8P2JzFw6EhMWGG+U8=");

        // TEST
        final EncryptedData testee = new EncryptedData(keyId, keyVersion, dataType, contentType, encryptedData);

        // VERIFY
        assertThat(testee.getKeyId()).isEqualTo(keyId);
        assertThat(testee.getKeyVersion()).isEqualTo(keyVersion);
        assertThat(testee.getDataType()).isEqualTo(dataType);
        assertThat(testee.getContentType()).isEqualTo(contentType);
        assertThat(testee.getEncryptedData()).isEqualTo(encryptedData);

    }

    @Test
    public final void testMarshalUnmarshalJson() {

        // PREPARE
        final EncryptedData original = createTestee(UUID.randomUUID());
        final JsonbConfig config = new JsonbConfig().withPropertyVisibilityStrategy(new FieldAccessStrategy())
                .withBinaryDataStrategy(BinaryDataStrategy.BASE_64);
        final Jsonb jsonb = JsonbBuilder.create(config);

        // TEST
        final String json = jsonb.toJson(original, EncryptedData.class);
        final EncryptedData copy = jsonb.fromJson(json, EncryptedData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);

    }

    @Test
    public final void testUnmarshalJson() {

        // PREPARE
        final EncryptedData original = createTestee(UUID.fromString("99417357-47dc-4d78-8eeb-c634891b5645"));
        final JsonbConfig config = new JsonbConfig().withPropertyVisibilityStrategy(new FieldAccessStrategy())
                .withBinaryDataStrategy(BinaryDataStrategy.BASE_64);
        final Jsonb jsonb = JsonbBuilder.create(config);
        final String json = "{\"content-type\":\"application/json; encoding=UTF-8; version=1\","
                + "\"data-type\":\"MySecretData\",\"encrypted-data\":\"d3tHrl9NbYKl1SdDh4AzI5egUZfP2qeCSyNbnLdg/K9feYZSdvoWf+Lz7UJVfntRUNK"
                + "O8u62OspvcgRRidOAKBFtsCx6U77n0ww7Xh4jbZ27AA3KyDTXB+70PUejSZ2AIDgnxxLhj2IWklOBX7OJO/WxGuT1bsnEUi2GLvE8siCRDa29N2Xm1bvUMp"
                + "yzdUY6yjfBft9Ju8vTgwarIpTUDrLDYH/P0+iJd3eM+I8gqffe19Jv+OrwuN7D3mLVATDMoZ39vUo0ovw5Gfy1S0U0ErxfcviVqWQRxI2Kp/8P2JzFw6EhMWGG+U8=\""
                + ",\"key-id\":\"99417357-47dc-4d78-8eeb-c634891b5645\",\"key-version\":\"v1\"}";

        // TEST
        final EncryptedData copy = jsonb.fromJson(json, EncryptedData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);

    }

    @Test
    public final void testMarshalUnmarshalXml() {

        // PREPARE
        final EncryptedData original = createTestee(UUID.randomUUID());

        // TEST
        final String xml = marshal(original, (XmlAdapter[]) null, EncryptedData.class);
        final EncryptedData copy = unmarshal(xml, (XmlAdapter[]) null, EncryptedData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);

    }

    @Test
    public final void testUnmarshalXml() {

        // PREPARE
        final EncryptedData original = createTestee(UUID.fromString("972060b0-a8fa-4f01-a731-6accad669948"));
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<eyncrypted-data><key-id>972060b0-a8fa-4f01-a731-6accad669948</key-id><key-version>v1</key-version>"
                + "<data-type>MySecretData</data-type><content-type>application/json; encoding=UTF-8; version=1</content-type>"
                + "<encrypted-data>d3tHrl9NbYKl1SdDh4AzI5egUZfP2qeCSyNbnLdg/K9feYZSdvoWf+Lz7UJVfntRUNKO8u62OspvcgRRidOAKBFtsCx6U77n0ww7Xh4jbZ27A"
                + "A3KyDTXB+70PUejSZ2AIDgnxxLhj2IWklOBX7OJO/WxGuT1bsnEUi2GLvE8siCRDa29N2Xm1bvUMpyzdUY6yjfBft9Ju8vTgwarIpTUDrLDYH/P0+iJd3eM+I8gqff"
                + "e19Jv+OrwuN7D3mLVATDMoZ39vUo0ovw5Gfy1S0U0ErxfcviVqWQRxI2Kp/8P2JzFw6EhMWGG+U8=</encrypted-data></eyncrypted-data>";

        // TEST
        final EncryptedData copy = unmarshal(xml, (XmlAdapter[]) null, EncryptedData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);

    }

    private EncryptedData createTestee(final UUID uuid) {
        final String keyId = uuid.toString();
        final String keyVersion = "v1";
        final String dataType = "MySecretData";
        final String contentType = "application/json; encoding=UTF-8; version=1";
        final byte[] encryptedData = Base64.getDecoder().decode(
                "d3tHrl9NbYKl1SdDh4AzI5egUZfP2qeCSyNbnLdg/K9feYZSdvoWf+Lz7UJVfntRUNKO8u62OspvcgRRidOAKBFtsCx6U77n0ww7Xh4jbZ27AA3KyDTXB+"
                        + "70PUejSZ2AIDgnxxLhj2IWklOBX7OJO/WxGuT1bsnEUi2GLvE8siCRDa29N2Xm1bvUMpyzdUY6yjfBft9Ju8vTgwarIpTUDrLDYH/P0+iJd3"
                        + "eM+I8gqffe19Jv+OrwuN7D3mLVATDMoZ39vUo0ovw5Gfy1S0U0ErxfcviVqWQRxI2Kp/8P2JzFw6EhMWGG+U8=");

        return new EncryptedData(keyId, keyVersion, dataType, contentType, encryptedData);

    }

}
// CHECKSTYLE:ON
