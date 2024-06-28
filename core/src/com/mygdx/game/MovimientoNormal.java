package com.mygdx.game;

public class MovimientoNormal implements MovimientoStrategy {
    @Override
    public void mover(Nave4 nave) {
        // Velocidad Normal
        nave.setMoveAmount(4);
        
        nave.moverIzq();
        nave.moverDerecha();
        nave.moverArriba();
        nave.moverAbajo();
        nave.añadirFriccion();
        nave.frenadoVelMin();
        nave.mantenerJugadorEnPantalla(nave.getX(), nave.getY());
    }
}
