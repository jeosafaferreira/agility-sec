<?php

$id = filter_input(INPUT_GET, 'id', FILTER_VALIDATE_INT);
$cli = $_GET['cli'];
$mac = filter_input(INPUT_GET, 'mac', FILTER_VALIDATE_MAC);
$output = isset($_GET['o']) ? $_GET['o'] : "";
$status = filter_input(INPUT_GET, 'q', FILTER_VALIDATE_BOOLEAN);
$status = ($status) ? "OK" : "ERRO";

$conn = mysqli_connect("localhost", "root", "root", "agility");

switch (filter_input(INPUT_GET, "method", FILTER_VALIDATE_INT)) {
    case "1":
        //LICENÇA
        $query = mysqli_query($conn, "SELECT count(*), status, prazo FROM clientes WHERE cli_id='$cli' AND mac='$mac'");
        $r = mysqli_fetch_array($query);
        if ($r[0] > 0) {
            echo "{";
            echo "\"status\": \"" . utf8_encode($r['status']) . "\",";
            echo "\"date\": \"" . utf8_encode($r['prazo']) . "\"";
            echo "}";
        } else {
            echo "{";
            echo "\"status\": \"0\",";
            echo "\"date\": \"1900-01-01\"";
            echo "}";
        }
        break;
    case "2":
        //SOLICITAÇÃO DE OPERAÇÃO
        $query = mysqli_query($conn, "SELECT * FROM updates WHERE cli_id='$cli' AND status = 'PENDENTE'");

        echo "{";
        $i = 0;
        while ($r = mysqli_fetch_array($query)) {

            echo "\"" . $i . "\":";
            echo "{";
            echo "\"id\":\"" . $r['id'] . "\",";
            echo "\"op\":\"" . $r['op'] . "\",";
            echo "\"command\":\"" . $r['command'] . "\"";
            echo "},";
            $i++;
        }

        echo "}";
        break;
    case "3":
        //ATUALIZA STATUS
        mysqli_query($conn, "UPDATE updates SET status='$status' WHERE id='$id'");
        break;
    case "4":
        //RETORNO DE STATUS + OUTPUT
        mysqli_query($conn, "UPDATE updates SET output='$output' WHERE id='$id'");
        echo mysqli_error();
        break;
    default:
        break;
}
