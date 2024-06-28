package com.mygdx.game;

public class MovimientoTurbo implements MovimientoStrategy {
    private static final float TURBO_FACTOR = 1.2f;  // Ajuste intermedio

    @Override
    public void mover(Nave4 nave) {
        
        // Velocidad turbo
        nave.setMoveAmount(10);
        
        nave.moverIzq();
        nave.moverDerecha();
        nave.moverArriba();
        nave.moverAbajo();
        nave.añadirFriccion();
        nave.frenadoVelMin();
        nave.mantenerJugadorEnPantalla(nave.getX(), nave.getY());

    }
}
