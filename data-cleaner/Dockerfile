FROM python:3.9

WORKDIR /usr/src/app

COPY . .
COPY ../py_common_di .

RUN pip install py_common_di/
RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 80

CMD ["python", "-u", "main.py"]
