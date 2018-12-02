# CRIS - Current Research Information System

El objetivo de esta practica es realizar una versión simplificada de un CRIS, usando un conjunto de datos público para alimentar la aplicación. En el apartado de Funcionalidades implementadas se puede ver el conjunto de características que ofrece la aplicación.

## Descripción

Este proyecto constituye un sistema web para la gestión y la agregación de información sobre la investigación técnica realizada en una organización (departamento de la universidad), capaz de manejar la información acerca de los resultados de la investigación técnica, tales como artículos en revistas, talleres, etc. Hay un interés creciente por este tipo de sistemas:
- para los investigadores: fácil acceso a la información y - en caso necesario - detectores para recoger más datos para mejorar la información incompleta o inconsistente,
- para los gerentes y administradores de investigación: permiten medir y analizar fácilmente las actividades de investigación y compararlas con otras,
- para los financiadores de la investigación: la optimización del proceso de financiación,
- para los empresarios y las organizaciones de transferencia de tecnología: fácil recuperación de nuevas ideas y tecnología en un ámbito de conocimiento y fácil identificación de los competidores y comparación con investigaciones previas similares, 
- para los medios y el público: fácil acceso a la información para la presentación de resultados de investigación en diferentes ámbitos.

## Elementos principales

Los principales elementos de información en CRIS corresponden a los resultados de las actividades de investigación y los investigadores y contexto de la organización en que trabajan. Un subconjunto básico de los tipos de información es la siguiente.

- **Resultado de investigación.** La publicación del artículo o el artículo de congreso, conteniendo toda la información necesaria para hacer referencia a un artículo publicado en papel o medios electrónicos. Existen varios formatos para la transferencia de esta información de forma estándar, como APA, ISO 690, MLA, y herramientas de perfilado de investigación cuyos formatos son reconocidos como estándares de facto: BibTeX, EndNote, RefMan, csv. Una forma de medir la calidad de un resultado de investigación consiste en contar el número de cita que recibe; en algunos países este es el indicativo preferido de calidad. En cualquier caso, la publicación, ya sea artículo o congreso, debe ser atribuido a sus autores. 
- **La actividad de investigación.** Proyecto de investigación; aunque hay muchas actividades interesantes en el proceso de investigación, solamente los proyectos financiados por organismos públicos en virtud de concurrencia competitiva son relevantes porque la obtención de estos proyectos demuestran la competencia de los investigadores en la organización, y los fondos consiguieron pueden aplicarse para cubrir los costes de la investigación, que permiten la incorporación de jóvenes a los equipos de investigación.
- **Perfil investigador.** Los investigadores son los miembros de la organización que realizan actividades de investigación y obtienen resultados de investigación. Cada investigador se identifica de manera unívoca, y la información acerca de su carrera, lleva a cabo actividades de investigación y se almacenan los resultados de la investigación obtenida por el investigador -ya sea solo, o trabajando con sus compañeros en el grupo o investigadores de otras instituciones. El investigador es el dueño de su perfil, por lo que fijará los niveles de visibilidad de la información que almacena el sistema de sus actividades y resultados, así que por ejemplo el investigador es capaz de leer y actualizar toda la información con respecto a sí mismo, pero puede limitar el acceso de otros miembros de la organización o del público en general, a esta información.
- **Perfil de grupo de investigación.** Por lo general los investigadores cooperan en las estructuras que la organización reconoce como asociaciones estables. Un grupo está compuesto por investigadores, por lo general en la misma organización, y el perfil de grupo está compuesto por la información de los perfiles de los investigadores que la componen -eliminando la información duplicada.

## Funcionalidades implementadas

Esta versión contituye una versión simplificada de un CRIS, utilizando datos de investigadores y publicaciones, no incluyendo la clasificación de investigadores en grupos. Estos datos se almacenan en una base de datos. La relación entre estos dos modelos de datos es "un investigador tiene publicaciones" y "una publicación tiene un primer autor que es un investigador".

El sistema incluye varias vistas en las que se puede consultar la información almacenada.
- Una vista en la que se muestren todos los investigadores del sistema.
- Una vista detallada de los investigadores, que muestre toda la información almacenada de los mismos y un listado de sus publicaciones.
- Una vista detallada de las publicaciones con todos sus detalles y enlaces a las vistas de sus autores.

Un investigador que visite la página con la vista de su perfil o publicaciones, o el administrador, pueden acceder a una serie de funciones.
- Modificar el perfil del investigador.
- Añadir publicaciones a un investigador.
- Modificar la información de una publicación.

El administrador del sistema puede acceder a un panel de control que le permite crear nuevos investigadores. En este panel también puede poblar (rellenar) la base de datos subiendo archivos csv al sistema. 

Además de estas funcionalidades básicas, el sistema cuenta con algunas funcionalidades avanzadas, que son las siguientes.
- Generación del Curriculum Vitae del investigador. Los investigadores tienen acceso a una funcionalidad que genera un documento pdf donde se recopila toda su información y publicaciones.
- Acceso automático a la API de Scopus para actualizar la información de las publicaciones.
- Importación de datos de una fuente externa. El sistema se conecta con una cola de mensajes (simulada) a través de la cual se envian nuevas publicaciones que se incluyen en el sistema.

## Despliegue

El sistema está desarrollado para su despliegue en la nube, en la plataforma de [Google Cloud Platform](https://console.cloud.google.com) utilizando App Engine para desplegar la aplicación web de servidor, Datastore para el servicio de almacenamiento y PubSub para el servicio de colas.

### Funcionalidades añadidas

Esta plataforma permite utilizar el sistema de autenticación y manejo de usuarios de Google (asociado a cuentas de gmail) que se ha incorporado al sistema CRIS. 

Otra funcionalidad que se ha incluido a la plataforma es el uso de correo electrónico, tanto envío como recepción. Los investigadores tienen la oportunidad de crear nuevas publicaciones en su perfil enviando un correo al sistema con los datos que deben añadirse. Además, se proporciona un método para enviar el Curriculum Vitae que genera el sistema.

En resumen, el despliegue en Google Cloud Platform implementa la siguiente funcionalidades adicionales a las ya implementadas.
- Autenticación mediante Google para entrar en el sistema.
- Creación de publicaciones por correo electrónico.
- Envío del Curriculum Vitae por correo electrónico.

> En la rama _simply_ se proporciona un sistema simplificado para su utilización en local sin las funcionalidades que permiten el despliegue en Google Cloud Platform. Esta aplicación utiliza la base de datos H2.

### Configuración

Para la configuración del despliegue es **necesario** incluir en la carpeta `resources` dos archivos para la interconexión con la API de Scopus y con Google.

- `credentials.json`. Archivo de credenciales del proyecto creado en Google que como mínimo debe incluir el nombre del proyecto creado.

  ```json
  {
	  "project_id": "[NOMBRE_DEL_PROYECTO]",
  }
  ```

- `elsevier.json`. Archivo de credenciales para la interconexión con la API de Scopus. Este archivo debe incluir lo siguiente.

  ```json
  {
      "label": "[LABEL]",
	  "key": "[API_KEY]",
	  "domains": [
		  "https://dev.elsevier.com"
	  ]
  }
  ```

Una vez creados ambos archivos, ya se puede desplegar el sistema en Google Cloud Platform utilizando App Engine. Para ello, habrá que crear una nueva aplicación con lenguaje de programación java y entorno estándar.
