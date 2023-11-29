import time
from kafka.Listener import Listener

if __name__ == "__main__":
    time.sleep(30)

    print("Initializing listener")

    listener = Listener(server='kafka', port=9092, group_id='data-cleaner')

    print("Listener initialized")

    try:
        listener.listen('job-service-event-channel')
    except KeyboardInterrupt:
        pass
    finally:
        listener.close_listener()
