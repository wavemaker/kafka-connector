package com.wavemaker.connector.kafka.producer.model;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 9/7/20
 */
public class Header {

    private String key;
    private byte[] value;

    public Header(String key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    public String key(){
        return this.key;
    }

    public byte[] value() {
        return this.value;
    }
}
