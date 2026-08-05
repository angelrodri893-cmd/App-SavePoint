package com.example.app_savepoint.data.mapper

import com.example.app_savepoint.data.local.JuegoGuardado
import com.example.app_savepoint.data.local.ObjetivoJuego
import com.example.app_savepoint.data.local.SesionJuego
import com.example.app_savepoint.domain.model.JuegoBiblioteca
import com.example.app_savepoint.domain.model.Objetivo
import com.example.app_savepoint.domain.model.Sesion

fun JuegoGuardado.aDominio() = JuegoBiblioteca(
    juegoId = juegoId,
    titulo = titulo,
    imagenUrl = imagenUrl,
    genero = genero,
    plataforma = plataforma,
    estado = estado,
    progreso = progreso,
    fechaAgregado = fechaAgregado
)

fun JuegoBiblioteca.aEntidad() = JuegoGuardado(
    juegoId = juegoId,
    titulo = titulo,
    imagenUrl = imagenUrl,
    genero = genero,
    plataforma = plataforma,
    estado = estado,
    progreso = progreso,
    fechaAgregado = fechaAgregado
)

fun SesionJuego.aDominio() = Sesion(
    sesionId = sesionId,
    juegoId = juegoId,
    tituloJuego = tituloJuego,
    fecha = fecha,
    duracionMinutos = duracionMinutos,
    progreso = progreso,
    nota = nota,
    fotoUri = fotoUri
)

fun Sesion.aEntidad() = SesionJuego(
    sesionId = sesionId,
    juegoId = juegoId,
    tituloJuego = tituloJuego,
    fecha = fecha,
    duracionMinutos = duracionMinutos,
    progreso = progreso,
    nota = nota,
    fotoUri = fotoUri
)

fun ObjetivoJuego.aDominio() = Objetivo(
    objetivoId = objetivoId,
    juegoId = juegoId,
    descripcion = descripcion,
    completado = completado,
    fechaCreacion = fechaCreacion
)

fun Objetivo.aEntidad() = ObjetivoJuego(
    objetivoId = objetivoId,
    juegoId = juegoId,
    descripcion = descripcion,
    completado = completado,
    fechaCreacion = fechaCreacion
)
