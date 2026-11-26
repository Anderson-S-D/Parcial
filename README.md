# Levantamiento del Servicio Web

### Paso 1: Crear y activar el entorno virtual

#### Linux
```bash
python3 -m venv venv
source venv/bin/activate
```

#### Windows
```cmd
python -m venv venv
venv\Scripts\activate
```

---

### Paso 2: Instalar dependencias
```bash
pip install -r requeriments.txt
```

---

### Paso 3: Poblar la base de datos
```bash
python seed.py
```

---

### Paso 4: Iniciar el servidor web
```bash
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

->`http://localhost:8000`
