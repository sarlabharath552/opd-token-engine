# 🏥 OPD Token Allocation Engine (Spring Boot)

A production-ready backend system for managing OPD token allocation in hospitals with
priority handling, elastic slot capacity, emergency insertion, and real-time reallocation.

🔗 Live URL: https://opd-token-engine-1.onrender.com

---

## 🚀 Features

- Doctor-wise OPD slot management
- Priority-based token allocation
  - PAID
  - EMERGENCY
  - FOLLOW_UP
  - WALK_IN
  - ONLINE
- Slot capacity enforcement
- Emergency patient insertion (override logic)
- Token cancellation & reallocation
- Real-time OPD variability handling
- RESTful APIs
- Dockerized & cloud deployed

---

## 🛠 Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- Docker
- Render (Cloud Deployment)
- GitHub (Version Control)

---

## 🧠 System Design Overview

- **Doctor** → Has multiple OPD slots
- **Slot** → Fixed time window with max capacity
- **Patient** → Can be normal, follow-up, or emergency
- **Token** → Allocated based on priority & availability

Priority Order:
