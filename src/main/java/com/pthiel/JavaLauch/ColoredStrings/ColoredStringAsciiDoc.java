package com.pthiel.JavaLauch.ColoredStrings;

public class ColoredStringAsciiDoc extends ColoredStringBase {
    String value;

    @Override
    public String build() {
        return "```asciidoc" + this.value + "\n```";
    }

    public ColoredStringAsciiDoc() {
        this.value = "";
    }

    public ColoredStringAsciiDoc addBlue(String value) {
        this.value += "\n= " + value + " =";
        return this;
    }

    public ColoredStringAsciiDoc addBlueAboveEq(String value) {
        this.value += "\n" + value + "\n" + "=".repeat(value.length());
        return this;
    }

    public ColoredStringAsciiDoc addBlueAboveEq(String value, int n) {
        this.value += "\n" + value + "\n" + "=".repeat(n);
        return this;
    }

    public ColoredStringAsciiDoc addBlueAboveDash(String value) {
        this.value += "\n" + value + "\n" + "-".repeat(value.length());
        return this;
    }

    public ColoredStringAsciiDoc addBlueAboveDash(String value, int n) {
        this.value += "\n" + value + "\n" + "-".repeat(n);
        return this;
    }

    public ColoredStringAsciiDoc addOrange(String value) {
        this.value += "\n[" + value + "]";
        return this;
    }

    public ColoredStringAsciiDoc addNormal(String value) {
        this.value += "\n" + value;
        return this;
    }
}
