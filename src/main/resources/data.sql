CREATE TABLE T_COIN_INFO (
  ID            INTEGER PRIMARY KEY AUTO_INCREMENT,
  CODE          VARCHAR(255) NOT NULL,
  NAME          varchar(255) NOT NULL,
  UPDATED_TIME	TIMESTAMP
);

INSERT INTO T_COIN_INFO VALUES(1, 'EUR', '歐元', CURRENT_TIMESTAMP);
INSERT INTO T_COIN_INFO VALUES(2, 'GBP', '英鎊', CURRENT_TIMESTAMP);
INSERT INTO T_COIN_INFO VALUES(3, 'USD', '美元', CURRENT_TIMESTAMP);