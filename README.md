# CPS Energy System ⚡

Este proyecto implementa un **sistema ciberfísico de gestión y predicción de energía** basado en agentes de IA. Su objetivo es optimizar la distribución de recursos energéticos, predecir la demanda y responder ante eventos climáticos o fallas en los equipos.

---

## 🚀 Características principales

- **Agentes Inteligentes (Backend - Spring Boot)**
  - `DemandPredictorAgent`: predice la demanda energética a partir de históricos y contexto.
  - `EnergyDistributorAgent`: gestiona y distribuye la energía entre las fuentes y consumidores.
  - `SourceSelectorAgent`: selecciona la fuente energética más adecuada en función de la situación.

- **Herramientas de soporte**
  - `ConsumptionHistoryTool`: analiza el historial de consumo.
  - `EnergyDistributionTool`: simula y gestiona la distribución.
  - `EnergySourcesAnalysisTool`: compara y evalúa fuentes energéticas.
  - `WeatherServiceTool`: incorpora datos meteorológicos en tiempo real.

- **API REST (Spring Boot)**
  - Predicción de eventos climáticos (`WeatherEventPredictionController`).
  - Predicción de fallos en equipos (`EquipmentEventPredictionController`).

- **Frontend (Next.js + TypeScript)**
  - Interfaz de usuario para interactuar con los agentes.
  - Componentes de visualización de fuentes energéticas, capacidad y distribución.
  - Secciones de chat para interacción con el sistema.
  - Integración con datos simulados (`energySourcesContext.json`, `eventsParams.json`, etc.).

---

## 🏗️ Diagrama de Arquitectura
![flujo-cps](https://github.com/user-attachments/assets/8c52c219-fd65-40c9-998a-78fb72ca8347)

---

## 📂 Estructura del proyecto

```
cps-energy-main/
 ├── backend/                     # Backend en Java (Spring Boot)
 │   ├── src/main/java/com/msdp/cps_system/
 │   │   ├── agent/               # Implementación de agentes inteligentes
 │   │   ├── agent/tools/         # Herramientas auxiliares
 │   │   ├── controller/          # Controladores REST
 │   │   ├── dto/                 # Objetos de transferencia de datos
 │   │   ├── config/              # Configuración (seguridad, OpenAPI)
 │   │   │── Enums/               # Enumeraciones del sistema
 │   │   │── Service/             # Lógica de negocio
 │   │   │── Utils/               # Funciones auxiliares
 │   │   └── CpsSystemApplication.java
 │   └── pom.xml                  # Dependencias Maven
 │
 ├── frontend/                    # Frontend en Next.js + TS
 │   ├── app/                     # Páginas y componentes principales
 │   │   ├── components/          # Componentes UI (cards, forms, sections)
 │   │   ├── types/               # Tipos TypeScript
 │   │   └── layout.tsx           # Layout principal
 │   ├── data/                    # Datos simulados para agentes
 │   ├── hooks/                   # Hooks de React personalizados
 │   ├── public/images/           # Recursos estáticos
 │   ├── package.json             # Dependencias del frontend
 │   ├── Constants/               # Constantes del sistema
 │   ├── Utils/                   # Funciones auxiliares
 │   └── next.config.ts           # Configuración de Next.js
 │
 └── README.md                    # Este archivo
```

---

## 🛠️ Requisitos

- **Backend**
  - Java 17+
  - Maven 3.9+
- **Frontend**
  - Node.js 18+
  - npm o yarn

---

## ▶️ Ejecución

### Backend (Spring Boot)
1. Ir a la carpeta del backend:
   ```bash
   cd backend
   ```
2. Ejecutar con Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
3. API disponible en:
   ```
   http://localhost:8080
   ```

### Frontend (Next.js + TypeScript)
1. Ir a la carpeta del frontend:
   ```bash
   cd frontend
   ```
2. Instalar dependencias:
   ```bash
   npm install
   ```
   o
   ```bash
   yarn install
   ```
3. Ejecutar en modo desarrollo:
   ```bash
   npm run dev
   ```
4. Interfaz disponible en:
   ```
   http://localhost:3000
   ```

---

## 📖 Documentación

- **Swagger (Backend)**:  
  Una vez ejecutado el backend, la documentación interactiva estará en:  
  ```
  http://localhost:8080/swagger-ui.html
  ```

- **Frontend**:  
  El frontend se conecta a los endpoints del backend y muestra visualizaciones interactivas de las predicciones y distribuciones energéticas.

---
