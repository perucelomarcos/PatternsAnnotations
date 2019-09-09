package br.me.patterns.annotation.processor.generator;

import com.google.testing.compile.CompilationRule;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.Serializable;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class BuilderGeneratorUnitTest {

    @Rule
    public CompilationRule compilationRule = new CompilationRule();

    private TypeElement getElementToApplyPattern() {
        return compilationRule.getElements().getTypeElement(BuilderGeneratorUnitTest.CarroBuilder.class.getCanonicalName());
    }

    @Test
    public void generatedFileContent_matchesTemplate() {
        TypeElement elementToApplyPattern = getElementToApplyPattern();
        JavaFile[] generatedFiles = BuilderGenerator.generate(elementToApplyPattern);

        assertThat(generatedFiles[0].toString()).isEqualTo(getExpectedFullBuilderClassString());
        assertThat(generatedFiles[1].toString()).isEqualTo(getExpectedFullBuiltClassString());
    }

    @Test
    public void generatedFileName_matchesFilePattern() {
        TypeElement elementToApplyPattern = getElementToApplyPattern();
        String targetFileName = BuilderGenerator.getTargetFileName(elementToApplyPattern);

        assertThat(targetFileName).isEqualTo(elementToApplyPattern.getSimpleName().toString() + BuilderGenerator.CREATED_CLASS_SUFFIX);
        assertThat(targetFileName).contains(BuilderGenerator.PATTERN_NAME);
    }

    @Test
    public void builderClass_isPublicAndExtendsOriginalClass() {
        TypeName mockType = ParameterizedTypeName.get(CarroBuilder.class);
        String originalFile = CarroBuilder.class.getCanonicalName();
        String fileToCreate = CarroBuilder.class.getSimpleName() + BuilderGenerator.CREATED_CLASS_SUFFIX;
        ClassName className = ClassName.get(CarroBuilder.class.getPackage().getName(), fileToCreate);

        TypeSpec.Builder classTypeBuilder = BuilderGenerator.getBuilderClassTypeSpec(mockType, className);
        TypeSpec classType = classTypeBuilder.build();

        assertThat(classType.modifiers).contains(Modifier.PUBLIC);
        assertThat(classType.superclass.toString()).isEqualTo(originalFile);
        assertThat(classType.name).isEqualTo(fileToCreate);
        assertThat(classType.toString()).isEqualTo(getExpectedBuilderClassString());
    }

    @Test
    public void builtClass_isBuilderClassNamePrefix() {
        String fileToCreate = CarroBuilder.class.getSimpleName() + BuilderGenerator.CREATED_CLASS_SUFFIX;
        ClassName builderClassName = ClassName.get(CarroBuilder.class.getPackage().getName(), fileToCreate);
        String expectedClassNameToBuild = fileToCreate.substring(0, fileToCreate.indexOf(BuilderGenerator.PATTERN_NAME));

        String classNameToBuild = BuilderGenerator.getClassNameToBuild(builderClassName);

        assertThat(classNameToBuild).isEqualTo(expectedClassNameToBuild);
    }

    @Test
    public void builtClass_isPublicAndSerializable() {
        String fileToCreate = CarroBuilder.class.getSimpleName() + BuilderGenerator.CREATED_CLASS_SUFFIX;
        ClassName className = ClassName.get(CarroBuilder.class.getPackage().getName(), fileToCreate);
        String fileToBuild = BuilderGenerator.getClassNameToBuild(className);

        TypeSpec.Builder classTypeBuilder = BuilderGenerator.getBuiltClassTypeSpec(className);
        TypeSpec classType = classTypeBuilder.build();

        assertThat(classType.modifiers).contains(Modifier.PUBLIC);
        assertThat(classType.superinterfaces).contains(ClassName.get(Serializable.class));
        assertThat(classType.name).isEqualTo(fileToBuild);
        assertThat(classType.toString()).isEqualTo(getExpectedBuiltClassString(fileToBuild));
    }

    @Test
    public void constructor_isPublic() {
        MethodSpec constructor = BuilderGenerator.getConstructorSpec();

        assertThat(constructor.modifiers).contains(Modifier.PUBLIC);
    }

    private String getExpectedBuilderClassString() {
        String originalClassName = CarroBuilder.class.getCanonicalName();
        String createdClassName = CarroBuilder.class.getSimpleName() + BuilderGenerator.CREATED_CLASS_SUFFIX;

        return String.format("" +
                        "public class %s extends %s {\n" +
                        "  public %s() {\n" +
                        "  }\n" +
                        "}\n",
                createdClassName,
                originalClassName,
                createdClassName
        );
    }

    private String getExpectedFullBuilderClassString() {
        return "" +
                "package br.me.patterns.annotation.processor.generator;\n" +
                "\n" +
                "import java.lang.String;\n" +
                "\n" +
                "public class CarroBuilder_ extends BuilderGeneratorUnitTest.CarroBuilder {\n" +
                "  public CarroBuilder_() {\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setCor(String cor) {\n" +
                "    super.cor=cor;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setMarca(String marca) {\n" +
                "    super.marca=marca;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setAnoFabricacao(int anoFabricacao) {\n" +
                "    super.anoFabricacao=anoFabricacao;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setModelo(String modelo) {\n" +
                "    super.modelo=modelo;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setCombustivel(String combustivel) {\n" +
                "    super.combustivel=combustivel;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setPlaca(String placa) {\n" +
                "    super.placa=placa;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setQuilometragem(String quilometragem) {\n" +
                "    super.quilometragem=quilometragem;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setArCondicionado(boolean arCondicionado) {\n" +
                "    super.arCondicionado=arCondicionado;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setDirecaoEletrica(boolean direcaoEletrica) {\n" +
                "    super.direcaoEletrica=direcaoEletrica;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setDirecaoHidraulica(boolean direcaoHidraulica) {\n" +
                "    super.direcaoHidraulica=direcaoHidraulica;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setVidrosEletricos(boolean vidrosEletricos) {\n" +
                "    super.vidrosEletricos=vidrosEletricos;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setArQuente(boolean arQuente) {\n" +
                "    super.arQuente=arQuente;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setSensorEstacionamento(boolean sensorEstacionamento) {\n" +
                "    super.sensorEstacionamento=sensorEstacionamento;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public CarroBuilder_ setRodasLigaLeve(boolean rodasLigaLeve) {\n" +
                "    super.rodasLigaLeve=rodasLigaLeve;\n" +
                "    return this;\n" +
                "  }\n" +
                "\n" +
                "  public Carro build() {\n" +
                "    Carro carro = new Carro();\n" +
                "    carro.cor = cor;\n" +
                "    carro.marca = marca;\n" +
                "    carro.anoFabricacao = anoFabricacao;\n" +
                "    carro.modelo = modelo;\n" +
                "    carro.combustivel = combustivel;\n" +
                "    carro.placa = placa;\n" +
                "    carro.quilometragem = quilometragem;\n" +
                "    carro.arCondicionado = arCondicionado;\n" +
                "    carro.direcaoEletrica = direcaoEletrica;\n" +
                "    carro.direcaoHidraulica = direcaoHidraulica;\n" +
                "    carro.vidrosEletricos = vidrosEletricos;\n" +
                "    carro.arQuente = arQuente;\n" +
                "    carro.sensorEstacionamento = sensorEstacionamento;\n" +
                "    carro.rodasLigaLeve = rodasLigaLeve;\n" +
                "    return carro;\n" +
                "  }\n" +
                "}\n";
    }

    private String getExpectedBuiltClassString(String fileToBuild) {
        return String.format("" +
                        "public class %s implements java.io.Serializable {\n" +
                        "  public %s() {\n" +
                        "  }\n" +
                        "}\n",
                fileToBuild,
                fileToBuild);
    }

    private String getExpectedFullBuiltClassString() {
        return "" +
                "package br.me.patterns.annotation.processor.generator;\n" +
                "\n" +
                "import java.io.Serializable;\n" +
                "import java.lang.Override;\n" +
                "import java.lang.String;\n" +
                "\n" +
                "public class Carro implements Serializable {\n" +
                "  public String cor;\n" +
                "\n" +
                "  public String marca;\n" +
                "\n" +
                "  public int anoFabricacao;\n" +
                "\n" +
                "  public String modelo;\n" +
                "\n" +
                "  public String combustivel;\n" +
                "\n" +
                "  public String placa;\n" +
                "\n" +
                "  public String quilometragem;\n" +
                "\n" +
                "  public boolean arCondicionado;\n" +
                "\n" +
                "  public boolean direcaoEletrica;\n" +
                "\n" +
                "  public boolean direcaoHidraulica;\n" +
                "\n" +
                "  public boolean vidrosEletricos;\n" +
                "\n" +
                "  public boolean arQuente;\n" +
                "\n" +
                "  public boolean sensorEstacionamento;\n" +
                "\n" +
                "  public boolean rodasLigaLeve;\n" +
                "\n" +
                "  public Carro() {\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public String toString() {\n" +
                "    return \"\" + \n" +
                "          \"Carro {\" +\n" +
                "          \"cor=\" + cor + \",\" +\n" +
                "          \"marca=\" + marca + \",\" +\n" +
                "          \"anoFabricacao=\" + anoFabricacao + \",\" +\n" +
                "          \"modelo=\" + modelo + \",\" +\n" +
                "          \"combustivel=\" + combustivel + \",\" +\n" +
                "          \"placa=\" + placa + \",\" +\n" +
                "          \"quilometragem=\" + quilometragem + \",\" +\n" +
                "          \"arCondicionado=\" + arCondicionado + \",\" +\n" +
                "          \"direcaoEletrica=\" + direcaoEletrica + \",\" +\n" +
                "          \"direcaoHidraulica=\" + direcaoHidraulica + \",\" +\n" +
                "          \"vidrosEletricos=\" + vidrosEletricos + \",\" +\n" +
                "          \"arQuente=\" + arQuente + \",\" +\n" +
                "          \"sensorEstacionamento=\" + sensorEstacionamento + \",\" +\n" +
                "          \"rodasLigaLeve=\" + rodasLigaLeve + \",\" +\n" +
                "          \"}\";\n" +
                "    }\n" +
                "  }\n";
    }

    class CarroBuilder {
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
}