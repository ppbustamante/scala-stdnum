# scala-stdnum

[![CI](https://github.com/ppbustamante/scala-stdnum/actions/workflows/scala.yml/badge.svg)](https://github.com/ppbustamante/scala-stdnum/actions/workflows/scala.yml)

A Scala library to parse, validate and reformat standard numbers and codes in different formats. It contains a large collection of number formats.

Basically any number or code that has some validation mechanism available or some common formatting is eligible for inclusion in this library.

## Available formats

Currently, this package supports the following formats:

- RUT (Rol Único Tributario, Chilean national tax number).
- DNI (Documento Nacional de Identidad, Argentinian national identity number).
- CUIT (Código Único de Identificación Tributaria, Argentinian tax number).
- CBU (Clave Bancaria Uniforme, Argentine bank account number).
- RFC (Registro Federal de Contribuyentes, Mexican tax number).
- CURP (Clave Única de Registro de Población, Mexican personal ID.)
- NIT (Número De Identificación Tributaria, Colombian identity code).
- CNPJ (Cadastro Nacional da Pessoa Jurídica, Brazilian company identifier).
- CPF (Cadastro de Pessoas Físicas, Brazilian national identifier).

These modules generally do not provide background information on the meaning and use of the specified numbers, only parsing and handling functions.

## Interface

All modules implement a common interface. For example for RUT validation:

```scala
import cl.mixin.stdnum.cl.RUT

RUT.validate("39.232.415-1")
val res0: Either[ValidationError, String] = Right(392324151)

RUT.validate("23.431.324-3")
val res1: Either[ValidationError, String] = Left(InvalidChecksum)
```

Most of these modules implement the following functions:

- `validate()` validate the correctness of the passed number and return a compact representation of the number invalid
  numbers are rejected with one of the exceptions from the stdnum.exceptions module
- `compact()` return a compact representation of the number or code this function generally does not do validation but
  may raise exceptions for wildly incorrect numbers
- `format()` return a formatted version of the number in the preferred format this function generally expects to be
  passed a valid number or code

Apart from the above, the module may add extra parsing, validation or conversion functions.

## Requirements

The modules should not require any external Scala library and should be pure Scala. The modules are developed and tested with Scala 3.
