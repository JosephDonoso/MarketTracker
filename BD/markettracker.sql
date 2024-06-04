-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-06-2024 a las 18:07:49
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `markettracker`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mercados`
--

CREATE TABLE `mercados` (
  `id_mercado` varchar(15) NOT NULL COMMENT 'Identificador del mercado consultado',
  `rut_cliente` varchar(15) NOT NULL COMMENT 'Identificador del cliente',
  `moneda_cambio` text DEFAULT NULL COMMENT 'Moneda de cambio',
  `moneda_pago` text DEFAULT NULL COMMENT 'Moneda de pago',
  `monto_min_orden` double DEFAULT NULL COMMENT 'Tamaño de orden mínimo aceptado',
  `ultimo_precio` double DEFAULT NULL COMMENT 'Precio de la última orden ejecutada',
  `min_precio_venta` double DEFAULT NULL COMMENT 'Menor precio de venta',
  `max_precio_compra` double DEFAULT NULL COMMENT 'Máximo precio de compra',
  `volumen` double DEFAULT NULL COMMENT 'Volumen transado en las últimas 24 horas',
  `variacion_precio_24h` float DEFAULT NULL COMMENT 'Cuanto ha variado el precio en las últimas 24 horas (porcentaje)',
  `variacion_precio_7d` float DEFAULT NULL COMMENT 'Cuanto ha variado el precio el los últimos 7 días (porcentaje)',
  `fecha_consulta` timestamp NULL DEFAULT NULL COMMENT 'Fecha de cuando realizo la consulta un cliente'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `rut` varchar(15) NOT NULL COMMENT 'RUT del usuario',
  `nombre` text DEFAULT NULL COMMENT 'Nombre del usuario'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`rut`, `nombre`) VALUES
('111', 'Joseph'),
('123', 'pablo'),
('222', 'Jorge'),
('333', 'Seba'),
('345', 'lopa');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `mercados`
--
ALTER TABLE `mercados`
  ADD PRIMARY KEY (`id_mercado`,`rut_cliente`),
  ADD KEY `rut_cliente` (`rut_cliente`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`rut`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `mercados`
--
ALTER TABLE `mercados`
  ADD CONSTRAINT `mercados_ibfk_1` FOREIGN KEY (`rut_cliente`) REFERENCES `usuarios` (`rut`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
