package com.pthiel.JavaLauch.ColoredStrings;

public class ColoredStringDiff {
    String value;

    public ColoredStringDiff() {
        this.value = "";
    }

    public String build() {
        return "```diff" + value + "\n```";
    }

    public ColoredStringDiff addGreen(String value) {
        this.value += "\n+" + value;
        return this;
    }

    public ColoredStringDiff addGreen(String value, boolean symmetric) {
        if (!symmetric) {
            return addGreen(value);
        }
        this.value += "\n+" + value + "+";
        return this;
    }

    public ColoredStringDiff addRed(String value) {
        this.value += "\n-" + value;
        return this;
    }

    public ColoredStringDiff addRed(String value, boolean symmetric) {
        if (!symmetric) {
            return addRed(value);
        }
        this.value += "\n-" + value + "-";
        return this;
    }

    public ColoredStringDiff addGrayDashes(String value) {
        this.value += "\n---" + value;
        return this;
    }

    public ColoredStringDiff addGrayDashes(String value, boolean symmetric) {
        if (!symmetric) {
            return addGrayDashes(value);
        }
        this.value += "\n---" + value + "---";
        return this;
    }

    public ColoredStringDiff addGrayTimes(String value) {
        this.value += "\n*** " + value;
        return this;
    }

    public ColoredStringDiff addGrayTimes(String value, boolean symmetric) {
        if (!symmetric) {
            return addGrayTimes(value);
        }
        this.value += "\n*** " + value + " ***";
        return this;
    }

    public ColoredStringDiff addNormal(String value) {
        this.value += "\n" + value;
        return this;
    }

}
