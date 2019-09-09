package br.me.patterns.model;

import br.me.patterns.annotation.Builder;

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
