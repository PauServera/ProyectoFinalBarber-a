Introducción

En el mundo actual, los avances tecnológicos están redefiniendo la forma en que interactuamos con los servicios y cambiando industrias, desde la atención médica hasta el entretenimiento. En este panorama dinámico, las barberías no son una excepción y están experimentando una evolución significativa en la forma en que operan sus negocios y atienden a sus clientes. La gestión eficaz de estas instalaciones se ha convertido en un aspecto importante para satisfacer la creciente demanda de servicios y optimizar el trabajo de los profesionales implicados.

Para abordar esta necesidad, se lanza BarberTeam, un sistema de gestión integral diseñado específicamente para barberías. El sistema está diseñado no solo para simplificar las operaciones diarias de estas instalaciones, sino también para transformar la experiencia de propietarios, peluqueros y clientes. Este informe examina las principales capas de esta aplicación, las tecnologías avanzadas que la respaldan y el impacto que esta aplicación pretende tener en la gestión y personalización de los servicios prestados por las barberías.
Contexto
BarberTeam se sitúa en un mercado que está experimentando un crecimiento constante, con una demanda cada vez mayor de servicios de barbería personalizados y de alta calidad. Además, en un mundo cada vez más conectado, donde la tecnología móvil es omnipresente, la necesidad de soluciones digitales para optimizar la gestión de los negocios es más evidente que nunca.
Si bien existen otras soluciones de gestión para barberías en el mercado, BarberTeam se distingue por su enfoque integral y su capacidad para adaptarse a las necesidades específicas de cada establecimiento. Al combinar tecnología de vanguardia con una comprensión profunda de las dinámicas de las barberías modernas, BarberTeam aspira a convertirse en la opción preferida para propietarios y barberos que buscan maximizar su eficiencia y mejorar la experiencia del cliente.
En resumen, BarberTeam no solo es una respuesta a las demandas actuales del mercado de las barberías, sino también una manifestación de la continua evolución de la industria hacia un futuro digitalizado y centrado en el cliente.


Objetivos

1. Analizar el contexto y las tendencias actuales en la industria de las barberías: Investigar la evolución de las barberías, su importancia en la cultura contemporánea y los desafíos que enfrentan en un mundo cada vez más digitalizado.
2. Evaluar las necesidades y demandas de gestión en las barberías: Identificar los problemas y áreas de mejora en la gestión de las barberías, tanto desde la perspectiva de los propietarios como de los barberos y clientes.
3. Diseñar una solución integral de gestión para barberías: Desarrollar un sistema de gestión completo que aborde las necesidades identificadas, priorizando la eficiencia operativa, la comunicación mejorada y la experiencia del cliente.
4. Implementar y probar el sistema propuesto: Construir y poner a prueba el sistema de gestión propuesto, asegurando su funcionalidad, usabilidad y capacidad para satisfacer las necesidades de los usuarios finales.
5. Evaluar el impacto del sistema en la gestión y experiencia del cliente: Realizar un análisis exhaustivo de cómo el sistema afecta la eficiencia operativa de las barberías, la satisfacción de los clientes y la rentabilidad del negocio.
6. Proporcionar recomendaciones para futuras mejoras y aplicaciones: Basado en los hallazgos y resultados obtenidos, ofrecer sugerencias para mejorar el sistema actual y posibles áreas de investigación y desarrollo futuro en el campo de la gestión de barberías.


Análisis de requisitos

Introducción al análisis.

El análisis de requisitos es una etapa fundamental en el proceso de desarrollo de software, ya que sienta las bases para el diseño, implementación y prueba de un sistema. El objetivo de los siguientes puntos es identificar, documentar y analizar exhaustivamente las necesidades y funcionalidades clave de la aplicación:

Requisitos funcionales.

Registro de usuarios (Dueños, Barberos y Clientes).
Inicio de sesión.
Gestión de horarios y disponibilidad de citas.
Reserva y cancelación de citas.
Gestión de barberías (Desde diferentes usuarios).
Gestión de citas.
Generación de informes semanales o mensuales.

Requisitos no funcionales.

Seguridad de los datos del usuario.
Intuición en la interfaz de usuario.
Tiempo de respuesta del sistema.
Escalabilidad.
Disponibilidad.


Diseño de arquitectura

Capa de presentación.

En esta capa se maneja la interacción directa con el usuario y se controla cómo se muestra la información en la pantalla.

Capa de lógica.

Esta capa contiene las clases que definen la lógica central de la aplicación, su objetivo es encapsular la lógica de manera cohesiva y reutilizable, lo que facilita la modificación  y la prueba de la 
funcionalidad principal de la App.

Capa de Acceso a Datos.

Esta capa se encarga de interactuar con la base de datos, facilita la separación de las preocupaciones y mejora la modularidad de la App.

Implementación.

El marco teórico de este proyecto proporciona una base conceptual sólida que sustenta el desarrollo de BarberTeam, abordando los siguientes aspectos clave:

1. Lenguajes de Programación.
Java. Proporciona un entorno robusto y orientado a objetos para la creación de aplicaciones móviles con un alto rendimiento y escalabilidad.
2. Lenguajes de Marcado.
XML. Se utiliza para el Desarrollo de aplicaciones Android para definir la estructura  del diseño de las interfaces de usuario mediante archivos de diseño (layouts).
3. Bibliotecas y Frameworks.
Firebase Authentication: Se emplea para la gestión de la autenticación de usuarios. Sistema seguro y fácil de usar para autenticar usuarios mediante correo electrónico y contraseña, así como también a través de proveedores de autenticación como Google o Facebook.
SendGrid API: Se utiliza para el envío de correos electrónicos dentro de la aplicación. Proporciona una manera eficiente de enviar correos electrónicos de confirmación de citas y otras notificaciones importantes a los usuarios. La implementación utiliza la librería OkHttp para realizar peticiones HTTP a la API de SendGrid.
4. Tecnologías de Base de Datos.
Firebase Firestore: Se utiliza como la base de datos principal del proyecto. Es una base de datos noSQL en tiempo real que proporciona una estructura flexible y escalable para almacenar y sincronizar datos en la nube en tiempo real.
Firebase Storage: Se utiliza para almacenar archivos multimedia, como imágenes y videos. Solución eficiente y segura para almacenar y recuperar archivos en la nube.


Funcionalidades

1. MainActivity
Función principal: Inicio de sesión.
Detalles:
Permite a los usuarios iniciar sesión con correo electrónico y contraseña.
Integración con Google Sign-In para autenticación mediante cuenta de Google.
Enlace para registro de nuevos usuarios.


2. PerfilActivity
Función principal: Gestión del perfil del usuario.
Detalles:
Muestra la información personal del usuario, como nombre, correo electrónico y número de teléfono.
Permite al usuario editar su información personal y actualizar su foto de perfil.
Funcionalidad para cambiar la contraseña.
Opción para cerrar sesión.

3. PrincipalActivity
Función principal: Pantalla principal de la aplicación.
Detalles:
Muestra un resumen de la barbería, incluyendo citas programadas y detalles relevantes.
Navegación a otras secciones de la aplicación, como la gestión de barberos y clientes.

4. RegisterActivity
Función principal: Registro de nuevos usuarios.
Detalles:
Contiene un formulario para que los nuevos usuarios se registren con su correo electrónico y contraseña.
Guarda la información del usuario en Firebase Authentication.

5. AgregarBarberoActivity
Función principal: Agregar nuevos barberos.
Detalles:
Permite al usuario ingresar información detallada sobre un nuevo barbero, incluyendo su nombre y detalles de contacto.
Almacena la información del barbero en Firestore.

6. InfoBarberiaActivity
Función principal: Muestra información detallada de una barbería específica.
Detalles:
Proporciona una vista detallada de la barbería seleccionada, incluyendo nombre, dirección y otros detalles relevantes.
7. DefinirHorarioActivity
Función Principal: Definir horarios de trabajo.
Detalles:
Permite al usuario definir y actualizar los horarios de trabajo de los barberos.
Guarda los horarios en Firestore para su gestión.

8. MisCitasActivity
Función principal: Gestión de citas del usuario.
Detalles:
Muestra una lista de todas las citas programadas por el usuario.
Permite al usuario cancelar o reprogramar citas.

9. SeleccionarBarberoActivity
Función principal: Selección de barbero para una cita.
Detalles:
Muestra una lista de barberos disponibles.
Permite al usuario seleccionar un barbero específico para una cita.

10. SeleccionarHoraActivity
Función principal: Selección de hora para una cita.
Detalles:
Muestra una lista de horas disponibles para citas.
Permite al usuario seleccionar una hora específica para su cita.

11. rolSelectorActivity
Función principal: Selección de rol del usuario.
Detalles:
Permite a los usuarios seleccionar su rol en la aplicación, como cliente o barbero.

12. Usuario
Función principal: Modelo de datos para Usuario.
Detalles:
Define los atributos de un usuario como nombre, correo electrónico y rol.

13. Barbería
Función principal: Modelo de datos para Barbería.
Detalles:
Define los atributos de una barbería como nombre, dirección, etc.

14. Barbero
Función principal: Modelo de datos para Barbero.
Detalles:
Define los atributos de un barbero como nombre, experiencia, etc.

15. Cliente
Función principal: Modelo de datos para Cliente.
Detalles:
Define los atributos de un cliente como nombre, contacto, etc.

16. Cita
Función principal: Modelo de datos para Cita.
Detalles:
Define los atributos de una cita como fecha, hora, barbero y cliente.

17. Dueño
Función principal: Modelo de datos para Dueño.
Detalles:
Define los atributos de un dueño de barbería como nombre, contacto, etc.

18. EmailService
Función principal: Servicio para el envío de correos electrónicos.
Detalles:
Permite enviar correos de confirmación de citas o notificaciones.

19. FirestoreManager
Función principal: Gestión de operaciones comunes en Firestore.
Detalles:
Contiene métodos para agregar, actualizar y eliminar documentos en Firestore.

20. BarberiaDialogAdapter
Función principal: Adaptador para mostrar una lista de barberías filtrada.
Detalles:
Configura el RecyclerView para mostrar los datos de cada barbería y gestiona las interacciones del usuario con la lista.

21. BarberiaAdapter
Función principal: Adaptador para mostrar una lista de barberías en un RecyclerView.
Detalles:
Configura el RecyclerView para mostrar los datos de cada barbería y gestiona las interacciones del usuario con la lista.

22. BarberoAdapter
Función principal: Adaptador para mostrar una lista de barberos en un RecyclerView.
Detalles:
Configura el RecyclerView para mostrar los datos de cada barbero y gestiona las interacciones del usuario con la lista.

23. CitasAdapter
Función principal: Adaptador para mostrar una lista de citas en un RecyclerView.
Detalles:
Configura el RecyclerView para mostrar los datos de cada cita y gestiona las interacciones del usuario con la lista.

24. HorasDisponiblesAdapter
Función principal: Adaptador para mostrar una lista de horas disponibles de un barbero.
Detalles:
Configura el RecyclerView para mostrar los datos de cada cita y filtrarlas.

Estas actividades y componentes representan las funcionalidades principales de la aplicación, permitiendo la gestión de usuarios, barberos, barberías, citas y otros elementos clave.

Pruebas y validación

Introducción.

El objetivo de esta sección es describir las pruebas realizadas para asegurar la calidad y el correcto funcionamiento del sistema BarberTeam. Se detallarán los diferentes tipos de pruebas, los métodos utilizados, los resultados obtenidos y las conclusiones derivadas de estas pruebas.
Plan de Pruebas.

Objetivos de las Pruebas:

Verificar que todas las funcionalidades del sistema funcionan según lo esperado.
Identificar y corregir errores y defectos en el software.
Asegurar la usabilidad y la satisfacción del usuario final.
Evaluar el rendimiento y la seguridad del sistema.

Tipos de Pruebas:

Pruebas Unitarias.


Descripción: Pruebas realizadas sobre las unidades más pequeñas del código (métodos y funciones) de manera individual.

Herramientas Utilizadas: JUnit para Java.

Ejemplos de Casos de Prueba:

Validación de la función de inicio de sesión.

Prueba de la gestión de horarios.

Verificación de la reserva y cancelación de citas.

Pruebas Funcionales.


Descripción: Verificación de que cada funcionalidad del sistema cumple con los requisitos especificados.

Método: Realización manual de casos de uso específicos.

Ejemplos de Casos de Prueba:

Registro de usuarios (dueños, barberos y clientes).
Inicio de sesión.
Gestión de horarios y disponibilidad de citas.
Reserva y cancelación de citas.
Generación de informes semanales o mensuales.

Pruebas de Usabilidad.


Descripción: Evaluación de la facilidad de uso y la experiencia del usuario con la aplicación.

Método: Observación directa y recopilación de feedback de usuarios que prueban la aplicación.
Aspectos Evaluados:

Facilidad de navegación en la aplicación.
Claridad de la interfaz de usuario.
Satisfacción general del usuario.

Pruebas de Rendimiento.

Descripción: Evaluación del rendimiento del sistema bajo diferentes condiciones de uso.
Método: Ejecución manual de acciones repetitivas y monitoreo del tiempo de respuesta y la estabilidad.

Métricas Evaluadas:

Tiempo de respuesta del sistema.
Comportamiento bajo múltiples solicitudes simultáneas.
Pruebas de Seguridad.
Descripción: Identificación de posibles vulnerabilidades de seguridad en el sistema.
Método: Intentos manuales de acceder a datos protegidos o realizar acciones no autorizadas.
Aspectos Evaluados:
Protección de datos de usuario.
Autenticación y autorización.

Mejoras Futuras

Introducción.

A medida que BarberTeam evoluciona, es importante considerar y planificar mejoras y características adicionales que puedan incrementar su valor y usabilidad. Este apartado describe posibles mejoras y extensiones que pueden implementarse en futuras versiones de la aplicación para continuar satisfaciendo las necesidades de los usuarios y mantenerse competitivos en el mercado.

Mejoras Sugeridas.

Optimización del Rendimiento.

Descripción: Mejorar la eficiencia del sistema, especialmente bajo cargas altas, para asegurar un tiempo de respuesta rápido y una experiencia de usuario fluida.
Acciones Propuestas:
Implementar técnicas de carga diferida y almacenamiento en caché.
Optimizar consultas a la base de datos.
Realizar pruebas de carga y escalabilidad más extensivas.

Integración con Redes Sociales.

Descripción: Permitir a los usuarios registrarse e iniciar sesión utilizando sus cuentas de redes sociales como Facebook, Google o Instagram.
Beneficios:
Facilitar el proceso de registro e inicio de sesión.
Incrementar la visibilidad y el alcance de la aplicación mediante la integración con plataformas populares.

Módulo de Marketing y Promociones.

Descripción: Desarrollar un módulo que permita a los propietarios de barberías crear y gestionar campañas de marketing y promociones dentro de la aplicación.
Características Incluidas:
Envío de notificaciones push para promociones.
Creación de cupones y descuentos.
Análisis de la efectividad de las campañas de marketing.

Sistema de Recomendaciones Personalizadas.

Descripción: Implementar un sistema que ofrezca recomendaciones personalizadas de servicios y productos basados en el historial y preferencias del cliente.
Acciones Propuestas:
Utilizar algoritmos de machine learning para analizar el comportamiento del usuario.
Integrar un motor de recomendaciones que sugiera servicios adicionales o productos relevantes.
Funcionalidades de Análisis y Reportes Avanzados.

Descripción: Ampliar las capacidades de generación de informes para incluir análisis más detallados y visualizaciones avanzadas.
Beneficios:
Proporcionar a los propietarios de barberías insights más profundos sobre el rendimiento del negocio.
Facilitar la toma de decisiones basada en datos.
Mejoras en la Interfaz de Usuario (UI) y Experiencia de Usuario (UX).

Descripción: Continuar refinando la interfaz y la experiencia de usuario para hacer la aplicación más intuitiva y agradable de usar.
Acciones Propuestas:
Realizar estudios de usabilidad adicionales y recoger feedback de los usuarios.
Implementar un diseño responsive para una mejor experiencia en dispositivos móviles y tablets.
Añadir tutoriales interactivos y una guía de usuario dentro de la aplicación.
Expansión de Funcionalidades de Pago.

Descripción: Integrar métodos de pago adicionales y permitir la gestión de pagos directamente desde la aplicación.
Beneficios:
Facilitar a los clientes la realización de pagos en línea.
Ofrecer a los propietarios una herramienta para gestionar transacciones y facturación de manera eficiente.

Bibliografía y Referencias

Introducción.
En esta sección se enumeran las fuentes de información y referencias bibliográficas que se han utilizado para la realización de este proyecto. Estas fuentes artículos, sitios web, y otras publicaciones relevantes que han contribuido al desarrollo de BarberTeam y a la fundamentación teórica y técnica del proyecto.
Artículos.

Bass, L., Clements, P., & Kazman, R. (2012). Software Architecture in Practice. Addison-Wesley. Este artículo ofrece una guía sobre la arquitectura de software y cómo diseñar sistemas escalables y eficientes.

Nielsen, J. (1993). Usability Engineering. Morgan Kaufmann. Un recurso clave sobre los principios de la usabilidad y cómo aplicarlos en el diseño de interfaces de usuario.
Sitios Web y Recursos en Línea
Documentación Técnica.

Firebase. (2023). Firebase Documentation. Disponible en: https://firebase.google.com/docs. Fuente primaria para la implementación de servicios de autenticación y bases de datos en tiempo real utilizadas en BarberTeam.

Java SE Documentation. (2023). The Java™ Tutorials. Disponible en: https://docs.oracle.com/javase/tutorial/. Referencia principal para el desarrollo en Java.

Android Developers. (2023). Android Developer Documentation. Disponible en: https://developer.android.com/docs. Guía completa para el desarrollo de aplicaciones móviles en Android.

Firebase. (2023). Firebase Firestore Documentation. Disponible en: https://firebase.google.com/docs/firestore. Fuente primaria para la implementación de la base de datos en tiempo real en BarberTeam.

Firebase. (2023). Firebase Storage Documentation. Disponible en: https://firebase.google.com/docs/storage. Documentación utilizada para el almacenamiento de archivos multimedia en la nube.
Conclusiones
Introducción.
En esta sección se presentan las conclusiones derivadas del desarrollo y la implementación del proyecto BarberTeam. Se destacan los logros alcanzados, las lecciones aprendidas y el impacto potencial de la aplicación en la gestión de barberías.

Logros Alcanzados.
Desarrollo de una Solución Integral.

Se ha logrado desarrollar e implementar una solución integral de gestión para barberías que aborda las necesidades específicas de propietarios, barberos y clientes. BarberTeam facilita la gestión de citas, la organización de horarios y la administración del negocio de manera eficiente.
Uso de Tecnologías Avanzadas.

La integración de tecnologías avanzadas como Firebase Authentication, Firestore y Storage ha permitido crear una aplicación robusta y escalable. Estas tecnologías aseguran la seguridad de los datos, la sincronización en tiempo real y la capacidad de almacenamiento eficiente.
Interfaz de Usuario Intuitiva.

Se ha diseñado una interfaz de usuario intuitiva y fácil de usar que mejora la experiencia del usuario final. La simplicidad y claridad de la interfaz permiten una navegación fluida y un uso eficiente de la aplicación.
Pruebas y Validación Exitosas.

Se han realizado pruebas exhaustivas de las funcionalidades del sistema, la usabilidad, el rendimiento y la seguridad. Los resultados positivos de estas pruebas confirman la calidad y fiabilidad de BarberTeam.

Planificación y Gestión del Proyecto.

La planificación detallada y el uso de un diagrama de Gantt han permitido una gestión eficaz del proyecto, asegurando que todas las tareas se completaran dentro del plazo previsto.
Lecciones Aprendidas

Importancia de la Planificación.

Una planificación detallada y bien estructurada es crucial para el éxito de un proyecto de desarrollo de software. El uso del diagrama de Gantt ayudó a mantener el proyecto en curso y a gestionar el tiempo de manera eficiente.
Adaptabilidad y Flexibilidad:

La capacidad de adaptarse a cambios y enfrentar desafíos imprevistos es esencial. Durante el desarrollo de BarberTeam, surgieron varios problemas que requirieron soluciones creativas y ajustes en el plan original.
Feedback del Usuario:

El feedback continuo de los usuarios es invaluable para mejorar la aplicación. Las pruebas de usabilidad y la recopilación de opiniones de los usuarios finales permitieron realizar mejoras significativas en la interfaz y la funcionalidad de la aplicación.
Impacto Potencial.
Mejora en la Gestión de Barberías.

BarberTeam tiene el potencial de transformar la gestión de barberías al ofrecer una herramienta completa y fácil de usar. Esto puede llevar a una mayor eficiencia operativa, una mejor organización y una mayor satisfacción del cliente.
Crecimiento del Negocio.

Al facilitar la gestión de citas y la comunicación con los clientes, BarberTeam puede contribuir al crecimiento del negocio, aumentando la retención de clientes y atrayendo a nuevos usuarios.
Futuro Digitalizado.

La implementación de BarberTeam representa un paso hacia la digitalización de las barberías, alineándose con las tendencias actuales del mercado y las expectativas de los clientes en un mundo cada vez más conectado.
Conclusión.
El desarrollo de BarberTeam ha sido un proyecto desafiante y gratificante que ha resultado en una aplicación innovadora y útil para la gestión de barberías. Los logros alcanzados y las lecciones aprendidas durante este proceso proporcionan una base sólida para futuras mejoras y expansiones de la aplicación. Con el continuo avance de la tecnología y las necesidades cambiantes del mercado, BarberTeam está bien posicionado para adaptarse y seguir ofreciendo valor a sus usuarios.


Agradecimientos
Quisiera expresar mi más sincero agradecimiento a todas las personas que me han apoyado y guiado durante el desarrollo de este proyecto. Sin su ayuda y orientación, este trabajo no habría sido posible.

Profesores.
Antoni Ginard:
Profesor de Programación. Sus enseñanzas en programación han sido fundamentales para superar los desafíos técnicos y alcanzar los objetivos planteados.

Oriol Gómez:
Profesor de Gestión Empresarial y Desarrollo de Interfaces. Agradezco profundamente sus consejos y sugerencias que me ayudaron a mejorar la usabilidad y funcionalidad de BarberTeam. Su enfoque práctico y sus enseñanzas sobre gestión y diseño de interfaces han sido de gran inspiración y motivación.

Germán Cantallops:
Profesor de Acceso a Datos y tutor. Gracias por su paciencia y dedicación al ofrecer su experiencia y conocimientos. Su guía como tutor ha sido inestimable para mantener el enfoque y la calidad del trabajo.

Otros Agradecimientos.
A mi familia y amigos, por su constante apoyo y ánimo durante todo este proceso. Su confianza en mí me ha impulsado a seguir adelante y dar lo mejor de mí en cada etapa del proyecto.

A todos los usuarios que participaron en las pruebas y ofrecieron su valioso feedback. Sus opiniones y sugerencias han sido cruciales para mejorar la aplicación y asegurar que satisface las necesidades reales de los usuarios finales.

A mis compañeros de clase, por su colaboración y por ser una fuente de motivación y apoyo durante el desarrollo de este proyecto.
