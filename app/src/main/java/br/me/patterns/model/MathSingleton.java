package br.me.patterns.model;

import br.me.patterns.annotation.Singleton;

@Singleton
public class MathSingleton {

    public int sum(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public float multiply(float a, float b) {
        return a * b;
    }

    public float divide(float a, float b) {
        return a / b;
    }

    MathSingleton() {
    }
}
