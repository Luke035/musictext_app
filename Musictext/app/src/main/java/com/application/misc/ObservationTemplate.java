package com.application.misc;

import java.util.List;

/**
 * Created by lucagrazioli on 18/06/15.
 */
public class ObservationTemplate {
    private String nome;
    private String realName;
    private List<String> values;
    private List<String> realValues;

    public ObservationTemplate(String nome, String realName, List<String> values, List<String> realValues) {
        this.nome = nome;
        this.realName = realName;
        this.values = values;
        this.realValues = realValues;
    }

    public ObservationTemplate(String nome, List<String> values) {
        this.nome = nome;
        this.values = values;
    }

    public String getNome() {
        return nome;
    }

    public List<String> getValues() {
        return values;
    }

    public String getRealName() {
        return realName;
    }

    public List<String> getRealValues() {
        return realValues;
    }
}
