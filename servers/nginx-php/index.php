<?php

$c = apc_fetch("mytestfile");

if ($c == NULL) {
     $c  = file_get_contents("../index.html");
     apc_add("mytestfile", $c);
}

echo $c;

?>
