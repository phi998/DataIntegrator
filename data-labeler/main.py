import time
from kafka.Listener import Listener

if __name__ == "__main__":
    time.sleep(30)

    print("Initializing listener")

    listener = Listener(server='kafka', port=9092, group_id='data-labeler')

    print("Listener initialized")

    try:
        listener.listen('cleaned-data-event-channel')
    except KeyboardInterrupt:
        pass
    finally:
        listener.close_listener()
