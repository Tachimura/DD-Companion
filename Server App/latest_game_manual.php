<?php
	#parametri
	$servername = "localhost";
	$username = "admin";
	$password = "admin";
	$dbname = "my_jdj0k3r";
	try {
		#eseguo connessione
		$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
		// set the PDO error mode to exception
		$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		#echo "Connected successfully\n";
		$sql = "SELECT * FROM `game_manuals` ORDER BY GM_ID DESC LIMIT 1";
		$stmt = $conn->prepare($sql);
		$stmt->execute();
		$result = $stmt->fetch(PDO::FETCH_ASSOC);
        // success
		$response["success"] = 1;
		$response["result"] = array();
		$responseStats["latest_manual_id"] = $result["GM_ID"];
		$responseStats["latest_manual_version"] = $result["GM_VERSION"];
        $responseStats["latest_manual_release_data"] = $result["GM_RELEASE_DATA"];
        // push stats into final response array
        array_push($response["result"], $responseStats);
        echo json_encode($response);
		#mi disconnetto
		$conn = null;
	} catch(PDOException $e) {
        // success
		$response["success"] = -1;
        $response["error_code"] = $e->getMessage();
        echo json_encode($response);
	}
?> 