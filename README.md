# PatternsAnnotations

This project is primarily intended to allow a student to see how Android's annotation processor features can be applied to build code based on those [Gang of Four (GoF) design patterns](https://en.wikipedia.org/wiki/Design_Patterns#Patterns_by_Type), thus also letting an Android programmer to code less line of code. "GoF" are well-known design patterns that describe how to solve recurring design problems in object-oriented software.

I have provided here the implementation of the *Singleton* and the *Builder* design patterns, but please feel free to review them or add implementation of the other design patterns.

Please also visit [this other GitHub repository](https://github.com/vinisauter/ParseAnnotation), from which I have found inspiration to start this project.

# A note about Unit Tests

Unit Tests can be found in the module 'patterns-processor'. I have provided the following ones: SingletonGeneratorUnitTest and BuilderGeneratorUnitTest, both with 100% of coverage :)

# Example 1: Singleton pattern annotation

For a comprehensive explanation on the Singleton Pattern, see: https://en.wikipedia.org/wiki/Singleton_pattern

A simple class annotated with @Singleton, like the one below...

    @Singleton
    public class MathSingleton {
        public int sum(int a, int b) { return a + b; }
        public int subtract(int a, int b) { return a - b; }
        public float multiply(float a, float b) { return a * b; }
        public float divide(float a, float b) { return a / b; }
        MathSingleton() { }
    }

... generates the following class:

    package br.me.patterns.model;

    public class MathSingleton_ extends MathSingleton {
      private static final MathSingleton INSTANCE = new MathSingleton();

      private MathSingleton_() {
      }

      public static MathSingleton getInstance() {
        return INSTANCE;
      }
    }


... that you can use like in the sample below:

    MathSingleton singletonObj = MathSingleton_.getInstance();
    float result = singletonObj.sum(num1, num2);
    log.append("Testing singleton-----\n");
    log.append(String.format("%s %s %s = %s", num1, operation, num2, result));
    log.append("\n-----------------------------------\n\n");


# Example 2: Builder pattern annotation

For a comprehensive explanation on the Builder Pattern, see: https://en.wikipedia.org/wiki/Builder_pattern

A simple class annotated with @Builder, like the one below...

    @Builder
    public class CarroBuilder {
        String cor;
        String marca;
        int anoFabricacao;
        String modelo;
        String combustivel;
        String placa;
        String quilometragem;
        boolean arCondicionado;
        boolean direcaoEletrica;
        boolean direcaoHidraulica;
        boolean vidrosEletricos;
        boolean arQuente;
        boolean sensorEstacionamento;
        boolean rodasLigaLeve;
    }

... generates the following classes:

    package br.me.patterns.model;

    import java.lang.String;

    public class CarroBuilder_ extends CarroBuilder {
      public CarroBuilder_() {
      }

      public CarroBuilder_ setCor(String cor) {
        super.cor=cor;
        return this;
      }

      public CarroBuilder_ setMarca(String marca) {
        super.marca=marca;
        return this;
      }

      public CarroBuilder_ setAnoFabricacao(int anoFabricacao) {
        super.anoFabricacao=anoFabricacao;
        return this;
      }

      public CarroBuilder_ setModelo(String modelo) {
        super.modelo=modelo;
        return this;
      }

      public CarroBuilder_ setCombustivel(String combustivel) {
        super.combustivel=combustivel;
        return this;
      }

      public CarroBuilder_ setPlaca(String placa) {
        super.placa=placa;
        return this;
      }

      public CarroBuilder_ setQuilometragem(String quilometragem) {
        super.quilometragem=quilometragem;
        return this;
      }

      public CarroBuilder_ setArCondicionado(boolean arCondicionado) {
        super.arCondicionado=arCondicionado;
        return this;
      }

      public CarroBuilder_ setDirecaoEletrica(boolean direcaoEletrica) {
        super.direcaoEletrica=direcaoEletrica;
        return this;
      }

      public CarroBuilder_ setDirecaoHidraulica(boolean direcaoHidraulica) {
        super.direcaoHidraulica=direcaoHidraulica;
        return this;
      }

      public CarroBuilder_ setVidrosEletricos(boolean vidrosEletricos) {
        super.vidrosEletricos=vidrosEletricos;
        return this;
      }

      public CarroBuilder_ setArQuente(boolean arQuente) {
        super.arQuente=arQuente;
        return this;
      }

      public CarroBuilder_ setSensorEstacionamento(boolean sensorEstacionamento) {
        super.sensorEstacionamento=sensorEstacionamento;
        return this;
      }

      public CarroBuilder_ setRodasLigaLeve(boolean rodasLigaLeve) {
        super.rodasLigaLeve=rodasLigaLeve;
        return this;
      }

      public Carro build() {
        Carro carro = new Carro();
        carro.cor = cor;
        carro.marca = marca;
        carro.anoFabricacao = anoFabricacao;
        carro.modelo = modelo;
        carro.combustivel = combustivel;
        carro.placa = placa;
        carro.quilometragem = quilometragem;
        carro.arCondicionado = arCondicionado;
        carro.direcaoEletrica = direcaoEletrica;
        carro.direcaoHidraulica = direcaoHidraulica;
        carro.vidrosEletricos = vidrosEletricos;
        carro.arQuente = arQuente;
        carro.sensorEstacionamento = sensorEstacionamento;
        carro.rodasLigaLeve = rodasLigaLeve;
        return carro;
      }
    }

... and...

    package br.me.patterns.model;

    import java.io.Serializable;
    import java.lang.Override;
    import java.lang.String;

    public class Carro implements Serializable {
      public String cor;

      public String marca;

      public int anoFabricacao;

      public String modelo;

      public String combustivel;

      public String placa;

      public String quilometragem;

      public boolean arCondicionado;

      public boolean direcaoEletrica;

      public boolean direcaoHidraulica;

      public boolean vidrosEletricos;

      public boolean arQuente;

      public boolean sensorEstacionamento;

      public boolean rodasLigaLeve;

      public Carro() {
      }

      @Override
      public String toString() {
        return "" +
              "Carro {" +
              "cor=" + cor + "," +
              "marca=" + marca + "," +
              "anoFabricacao=" + anoFabricacao + "," +
              "modelo=" + modelo + "," +
              "combustivel=" + combustivel + "," +
              "placa=" + placa + "," +
              "quilometragem=" + quilometragem + "," +
              "arCondicionado=" + arCondicionado + "," +
              "direcaoEletrica=" + direcaoEletrica + "," +
              "direcaoHidraulica=" + direcaoHidraulica + "," +
              "vidrosEletricos=" + vidrosEletricos + "," +
              "arQuente=" + arQuente + "," +
              "sensorEstacionamento=" + sensorEstacionamento + "," +
              "rodasLigaLeve=" + rodasLigaLeve + "," +
              "}";
        }
      }

... that you can use like in the sample below:

            CarroBuilder_ carBuilder = new CarroBuilder_()
                    .setAnoFabricacao(2013)
                    .setArCondicionado(true)
                    .setArQuente(true)
                    .setCombustivel("Diesel")
                    .setCor("Preto")
                    .setMarca("Ferrari")
                    .setQuilometragem("15km")
                    .setRodasLigaLeve(true)
                    .setSensorEstacionamento(false);

            Carro carro = carBuilder.build();

            log.append("Testing builder-----\n");
            log.append(carro.toString());
            log.append("\n-----------------------------------\n\n");


# A note about Unit Tests

Unit Tests can be found in the module 'patterns-processor'. I have provided the following ones: SingletonGeneratorUnitTest and BuilderGeneratorUnitTest, both with 100% of coverage :)


