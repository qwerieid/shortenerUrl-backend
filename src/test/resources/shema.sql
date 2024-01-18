DROP TABLE IF EXISTS `url`;
CREATE TABLE `url` (
  `createdat` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `originalurl` varchar(255) DEFAULT NULL,
  `shortenedurl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
