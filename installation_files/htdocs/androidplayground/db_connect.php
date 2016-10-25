<?php

/**
 * A class file to connect to database
 */
class DB_CONNECT {
    function connect() {
        require_once __DIR__ . '/db_config.php';
				$mysqli = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
				$mysqli->query('SET CHARACTER SET utf8');
				return $mysqli;
    }
}

?>