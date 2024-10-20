# Db model

## pokemon

id: int
name: string
image: string
attack: int
special_attack: int
defense: int
special_defense: int
hp: int
speed: int
type_1: string
type_2: string
discovered: bool; pour le pokedex

## own_pokemon

id: int
pokemon_id: int
level: int
name: string; pour les renommés

## inventory

id: int
object_id: int
quantity: int

## object

id: int
name: int
image: string
