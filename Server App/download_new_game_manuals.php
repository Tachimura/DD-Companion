<?php
	#parametri
	$servername = "localhost";
	$username = "admin";
	$password = "admin";
	$dbname = "my_jdj0k3r";
    $deviceMID = $_POST["manual_id"];
    if(!isset($deviceMID)){
    // success
		$response["success"] = -1;
        $response["error_code"] = "manual_id not_set";
        echo json_encode($response);
        return;
    }
	try {
		#eseguo connessione
		$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
		// set the PDO error mode to exception
		$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION, PDO::MYSQL_ATTR_INIT_COMMAND, "SET NAME'utf8'");
		#echo "Connected successfully\n";
        #prendo i manuali nuovi con le note
		$sql = "SELECT * FROM `game_manuals` WHERE GM_ID > ?;";
		$stmt = $conn->prepare($sql);
        $stmt->execute(array($deviceMID));
        // success
		$response["success"] = 1;
        
        
        //mi prendo tutti i linguaggi presenti
        $response["languages"] = array();
        $sqlLanguages = "SELECT * FROM `languages`;";
		$stmtLanguages = $conn->prepare($sqlLanguages);
        $stmtLanguages->execute();
        while ($rowLanguage = $stmtLanguages->fetch(PDO::FETCH_ASSOC)) {
          	$language = array();
            $language["lan_id"] = $rowLanguage["L_ID"];
            $language["lan_name"] = $rowLanguage["L_NAME"];
            array_push($response["languages"], $language);
        }
        
        //mi preparo i manuali
		$response["result"] = array();
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
       		$manual = array();
          	$manual["id"] = $row["GM_ID"];
          	$manual["version"] = $row["GM_VERSION"];
          	$manual["release_data"] = $row["GM_RELEASE_DATA"];
          	$manual["usable_points"] = $row["GM_USABLE_POINTS"];
          	$manual["base_str"] = $row["GM_BASE_FOR"];
          	$manual["base_dex"] = $row["GM_BASE_DEX"];
          	$manual["base_cos"] = $row["GM_BASE_COS"];
          	$manual["base_int"] = $row["GM_BASE_INT"];
          	$manual["base_sag"] = $row["GM_BASE_SAG"];
          	$manual["base_car"] = $row["GM_BASE_CAR"];
            
            
            //mi prendo le note del manuale nei vari linguaggi
            $manual["notes"] = array();
            $sqlNotes = "SELECT * FROM `game_manual_notes` JOIN `languages` ON GM_N_LAN = L_ID WHERE GM_N_ID = ?;";
			$stmtNotes = $conn->prepare($sqlNotes);
        	$stmtNotes->execute(array($manual["id"]));
        	while ($rowNote = $stmtNotes->fetch(PDO::FETCH_ASSOC)) {
            	$note = array();
                $note["lan_id"] = $rowNote["GM_N_LAN"];
                //$note["lan_name"] = $rowNote["L_NAME"];
          		$note["note"] = $rowNote["GM_N_NOTES"];
            	array_push($manual["notes"], $note);
            }
            
           //mi prendo tutte le razze del manuale
            $sql2 = "SELECT * FROM `game_manual_races` JOIN `races` ON GM_R_ID_RACE = R_ID WHERE GM_R_ID_MANUAL = ?;";
            $stmt2 = $conn->prepare($sql2);
            $stmt2->execute(array($manual["id"]));
            $manual["races"] = array();
        	while ($row2 = $stmt2->fetch(PDO::FETCH_ASSOC)) {
            	$race = array();
            	$race["id"] = $row2["GM_R_ID_RACE"];
                $race["new"] = $row2["GM_R_NEW"];
                //se è nuova invio anche le informazioni sulla razza
                if($row2["GM_R_NEW"] == 1){
            		$race["id_name"] = $row2["R_ID_NAME"];
            		$race["mod_str"] = $row2["R_MOD_STR"];
            		$race["mod_dex"] = $row2["R_MOD_DEX"];
            		$race["mod_cos"] = $row2["R_MOD_COS"];
            		$race["mod_int"] = $row2["R_MOD_INT"];
            		$race["mod_sag"] = $row2["R_MOD_SAG"];
            		$race["mod_car"] = $row2["R_MOD_CAR"];
                    
                    
                    //prendo il nome della razza in tutte le sue traduzioni
                    $race["names"] = array();
                    $sqlRaceNames = "SELECT * FROM `races_description` WHERE RD_ID = ?;";
                    $stmtRaceNames = $conn->prepare($sqlRaceNames);
                    $stmtRaceNames->execute(array($race["id"]));
                    while ($rowRaceName = $stmtRaceNames->fetch(PDO::FETCH_ASSOC)) {
                        $raceName = array();
                        $raceName["race_lan_id"] = $rowRaceName["RD_LAN"];
                        $raceName["race_name"] = $rowRaceName["RD_NAME"];
                        array_push($race["names"], $raceName);
                    }
                    
                }
            	array_push($manual["races"], $race);
            }
            #leggo tutte le classi del manuale
            $sql3 = "SELECT * FROM `game_manual_classes` JOIN `classes` ON GM_C_ID_CLASS = C_ID WHERE GM_C_ID_MANUAL = ?;";
            $stmt3 = $conn->prepare($sql3);
            $stmt3->execute(array($manual["id"]));
            $manual["classes"] = array();
        	while ($row3 = $stmt3->fetch(PDO::FETCH_ASSOC)) {
            	$class = array();
            	$class["id"] = $row3["GM_C_ID_CLASS"];
                //se è nuova invio anche le informazioni sulla razza
                $class["new"] = $row3["GM_C_NEW"];
                if($row3["GM_C_NEW"] == 1){
            		$class["id_name"] = $row3["C_ID_NAME"];
            		$class["hpdice"] = $row3["C_HD"];
            		$class["hpdice_next"] = $row3["C_HD_NEXT"];
            		$class["main_mod"] = $row3["C_MAIN"];
            		$class["salv_1"] = $row3["C_S1"];
            		$class["salv_2"] = $row3["C_S2"];
            		$class["ability_points"] = $row3["C_ABILITY_POINTS"];
                    //allineamenti del pg
            		$class["alg_lb"] = $row3["C_LB"];
            		$class["alg_ln"] = $row3["C_LN"];
            		$class["alg_lm"] = $row3["C_LM"];
            		$class["alg_nb"] = $row3["C_NB"];
            		$class["alg_nn"] = $row3["C_NN"];
            		$class["alg_nm"] = $row3["C_NM"];
            		$class["alg_cb"] = $row3["C_CB"];
            		$class["alg_cn"] = $row3["C_CN"];
            		$class["alg_cm"] = $row3["C_CM"];
                    
                    
                    //prendo il nome della classe in tutte le sue traduzioni
                    $class["names"] = array();
                    $sqlClassNames = "SELECT * FROM `classes_description` WHERE CD_ID = ?;";
                    $stmtClassNames = $conn->prepare($sqlClassNames);
                    $stmtClassNames->execute(array($class["id"]));
                    while ($rowClassName = $stmtClassNames->fetch(PDO::FETCH_ASSOC)) {
                        $className = array();
                        $className["class_lan_id"] = $rowClassName["CD_LAN"];
                        $className["class_name"] = $rowClassName["CD_NAME"];
                        array_push($class["names"], $className);
                    }
                    
                }
            	array_push($manual["classes"], $class);
            }
            
            #leggo tutti i talenti del manuale
            $sql4 = "SELECT * FROM `game_manual_talents` JOIN `talents` ON GM_T_ID_TALENT = T_ID WHERE GM_T_ID_MANUAL = ?;";
            $stmt4 = $conn->prepare($sql4);
            $stmt4->execute(array($manual["id"]));
            $manual["talents"] = array();
        	while ($row4 = $stmt4->fetch(PDO::FETCH_ASSOC)) {
            	$talent = array();
            	$talent["id"] = $row4["T_ID"];
                //se è nuova invio anche le informazioni sulla razza
                $talent["new"] = $row4["GM_T_NEW"];
                if($talent["new"] == 1){
            		$talent["id_name"] = $row4["T_ID_NAME"];
            		$talent["min_lvl"] = $row4["T_MIN_LVL"];
            		$talent["mod_str"] = $row4["T_MOD_STR"];
            		$talent["mod_dex"] = $row4["T_MOD_DEX"];
            		$talent["mod_cos"] = $row4["T_MOD_COS"];
            		$talent["mod_int"] = $row4["T_MOD_INT"];
            		$talent["mod_sag"] = $row4["T_MOD_SAG"];
            		$talent["mod_car"] = $row4["T_MOD_CAR"];
                    //allineamenti del pg
            		$talent["sal_tem"] = $row4["T_SAL_TEM"];
            		$talent["sal_rif"] = $row4["T_SAL_RIF"];
            		$talent["sal_vol"] = $row4["T_SAL_VOL"];
            		$talent["mod_tc"] = $row4["T_MOD_TC"];
            		$talent["mod_ca"] = $row4["T_MOD_CA"];
                    
                    
                    //prendo il nome della classe in tutte le sue traduzioni
                    $talent["description"] = array();
                    $sqlTalentNames = "SELECT * FROM `talents_description` WHERE TD_ID = ?;";
                    $stmtTalentNames = $conn->prepare($sqlTalentNames);
                    $stmtTalentNames->execute(array($talent["id"]));
                    while ($rowTalentD = $stmtTalentNames->fetch(PDO::FETCH_ASSOC)) {
                        $talentD = array();
                        $talentD["talent_lan_id"] = $rowTalentD["TD_LAN"];
                        $talentD["talent_name"] = $rowTalentD["TD_NAME"];
                        $talentD["talent_description"] = $rowTalentD["TD_DESCRIPTION"];
                        array_push($talent["description"], $talentD);
                    }
                    
                }
            	array_push($manual["talents"], $talent);
            }
            
            //inserisco manuale nei risultati
        	array_push($response["result"], $manual);
        }
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