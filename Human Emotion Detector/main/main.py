import cv2 # Importam OpenCV pentru procesarea imaginilor
import tkinter as tk # Importam tkinter pentru interfata grafica
from fer import FER # Importam FER pentru detectia emotiilor
from deepface import DeepFace # Importam Deepface pentru detectia emotiilor


"""

Momentan e de 8. daca pot face o noua emotie -> 10.

"""





class WarningWindow:
    def __init__(self, master):
        self.master = master
        self.master.title("Warning!")
        self.master.configure(bg="#000000")

        # Mesajul de atentionare
        warning_label = tk.Label(self.master,
                                 text="Atentie mare! \nAcest proiect a fost realizat de catre un student.\n Grija mare, ca poate pusca!.",
                                 font=("Helvetica", 12), bg="#000000", fg="#FFFFFF")
        warning_label.pack(pady=10)

        continue_button = tk.Button(self.master, text="Aia e", command=self.show_main_window, bg="#4CAF50", fg="white",
                                    font=("Helvetica", 12, "bold"))
        continue_button.pack(pady=5)

        # il centram
        window_width = 500
        window_height = 150
        screen_width = self.master.winfo_screenwidth()
        screen_height = self.master.winfo_screenheight()
        x_position = (screen_width - window_width) // 2
        y_position = (screen_height - window_height) // 2
        self.master.geometry(f"{window_width}x{window_height}+{x_position}+{y_position}")

    def show_main_window(self): #arata fereastra principala
        self.master.destroy()  # inchide fereastra de atentionare
        root = tk.Tk()
        app = EmotionDetectorGUI(root)
        root.mainloop()


class EmotionDetectorGUI:
    def __init__(self, master):
        self.master = master
        self.master.title("Real-time Emotion Detection")
        self.master.configure(bg="#000000")  # Set background color to black

        # Mesajul de bun venit
        top_label = tk.Label(self.master, text="Welcome",font=("Helvetica", 12), bg="#000000", fg="#FFFFFF")
        top_label.pack(side='top')

        # Avem un color_map pentru a putea colora fetele in functie de emotie
        self.color_map = {
            "angry": (0, 0, 255),  # rosu pt angry
            "happy": (0, 255, 0),  # verde pt happy
            "sad": (255, 0, 0),  # albastru pt sad
            "neutral": (255, 255, 255),  # alb pt neutral
            "surprise": (255, 255, 0),  # galben pt surprise
            "fear": (128, 0, 128),  # mov pt fear
            "disgust": (0, 128, 128)  # albastru deschis pt disgust
        }

        # centram fereastra
        window_width = 400
        window_height = 200
        screen_width = self.master.winfo_screenwidth()
        screen_height = self.master.winfo_screenheight()
        x_position = (screen_width - window_width) // 2
        y_position = (screen_height - window_height) // 2
        self.master.geometry(f"{window_width}x{window_height}+{x_position}+{y_position}") #setam dimensiunile si pozitia

        # facem un frame pentru butoane
        self.button_frame = tk.Frame(self.master, bg="#000000")  # Set background color to black
        self.button_frame.pack(expand=True)

        # Start cu functia de detectie FER
        button_width = 17
        button_height = 1
        self.start_button = tk.Button(self.button_frame, text="Start", command=self.start_detection, bg="#4CAF50",
                                      fg="white",
                                      width=button_width, height=button_height, font=("Helvetica", 14, "bold"))
        self.start_button.pack(pady=6)

        #"Start (Deepface Beta)" button
        self.deepface_button = tk.Button(self.button_frame, text="Start (Deepface Beta)",
                                         command=self.open_deepface_popup, bg="#FF4C50", fg="white",
                                         width=button_width, height=button_height, font=("Helvetica", 14, "bold"))
        self.deepface_button.pack(pady=6)

        # Initializam cascadele de fete si detectorul FER
        # de ce cascadele de fete? pt ca vrem sa detectam fetele in imagini
        self.face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml') # folosim un clasificator pentru a detecta fetele
        self.detector = FER() # folosim FER pentru a detecta emotiile

    def start_detection(self):
        # Incepe capturarea video
        cap = cv2.VideoCapture(0)

        while True:
            # capturam video-ul in Frames(cadre)
            ret, frame = cap.read()

            # Facem Frame-ul GrayScale daca nu este deja
            if len(frame.shape) == 3:  # Verificam numarul de canale. De ce 3? pt ca un frame color are 3 canale(Rosu, Verde, Albastru)
                gray_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            else:
                gray_frame = frame  # Daca este deja grayscale

            # Detectam fetele in frame-ul grayscale
            faces = self.face_cascade.detectMultiScale(gray_frame, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))

            # Procesam fiecare fata detectata
            for (x, y, w, h) in faces:
                # Extragem fata ROI. Ce e ROI? Region of Interest(Regiunea de Interes)
                face_roi = gray_frame[y:y + h, x:x + w]

                # Convertim fata ROI in format RGB(De ce? Pentru ca FER asteapta un frame color)
                face_roi_rgb = cv2.cvtColor(face_roi, cv2.COLOR_GRAY2RGB)

                # Folosim FER pentru a prezice emotia
                emotion, _ = self.detector.top_emotion(face_roi_rgb)

                # Alegem culoarea pentru emotia detectata. De ce e setat pe Alb? Pentru ca alb e Neutru
                color = self.color_map.get(emotion, (255, 255, 255))  # Default to white if emotion not found

                # Desenam un dreptunghi in jurul fetei si afisam emotia prezisa
                cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2) # Desenam dreptunghiul
                cv2.putText(frame, emotion, (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.9, color, 2) # Afisam emotia

            # Afisam frame-ul rezultat
            cv2.imshow('Real-time Emotion Detection', frame)

            # Apasam 'q' sau 'esc' pentru a iesi
            if cv2.waitKey(1) & 0xFF == ord('q') or cv2.waitKey(1) == 27:
                break

        # Eliberam captura si inchidem toate ferestrele. De ce eliberam? Pentru ca nu mai avem nevoie de captura(si ocupa memorie degeaba)
        cap.release()
        cv2.destroyAllWindows()

    def start_deepface_detection(self): #functie pentru a incepe detectia cu Deepface
        # Start Camera Video
        cap = cv2.VideoCapture(0)

        while True:
            # Transmiterea video-ului in cadre
            ret, frame = cap.read()

            # Convertim Frame-ul in Grayscale daca nu este deja
            if len(frame.shape) == 3:  # Verificam numarul de canale. De ce 3? pt ca un frame color are 3 canale(Rosu, Verde, Albastru)
                gray_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            else:
                gray_frame = frame  # Daca este deja grayscale

            # Detectam fetele in frame-ul grayscale
            faces = self.face_cascade.detectMultiScale(gray_frame, scaleFactor=1.1, minNeighbors=5,
                                                       minSize=(30, 30))

            # Procesam fiecare fata detectata
            for (x, y, w, h) in faces:
                # Extragem fata ROI. Ce e ROI? Region of Interest(Regiunea de Interes)
                face_roi = gray_frame[y:y + h, x:x + w]

                # Convertim fata ROI in format RGB(De ce? Pentru ca Deepface asteapta un frame color)
                face_roi_rgb = cv2.cvtColor(face_roi, cv2.COLOR_GRAY2RGB)

                # Folosim Deepface pentru a prezice emotia
                result = DeepFace.analyze(face_roi_rgb, actions=['emotion'], enforce_detection=False)

                # Determinam emotia dominanta
                emotion = result[0]['dominant_emotion']  # Alegem emotia dominanta din rezultat

                # Alegem culoarea pentru emotia detectata. De ce e setat pe Alb? Pentru ca alb e Neutru
                color = self.color_map.get(emotion, (255, 255, 255))

                # Desenam un dreptunghi in jurul fetei si afisam emotia prezisa
                cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2)
                cv2.putText(frame, emotion, (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.9, color, 2)

            # Afisam frame-ul rezultat
            cv2.imshow('Real-time Emotion Detection (Deepface Beta)', frame)

            # Apasam 'q' sau 'esc' pentru a iesi
            if cv2.waitKey(1) & 0xFF == ord('q') or cv2.waitKey(1) == 27:
                break

        # Eliberam captura si inchidem toate ferestrele. De ce eliberam? Pentru ca nu mai avem nevoie de captura(si ocupa memorie degeaba)
        cap.release()
        cv2.destroyAllWindows()

    def open_deepface_popup(self):
        # Creem o fereastra de tip popup pentru a avertiza utilizatorul
        deepface_popup = tk.Tk()
        deepface_popup.title("Deepface Warning")

        # Centram fereastra
        popup_width = 450
        popup_height = 200
        screen_width = deepface_popup.winfo_screenwidth()
        screen_height = deepface_popup.winfo_screenheight()
        x_position = (screen_width - popup_width) // 2
        y_position = (screen_height - popup_height) // 2
        deepface_popup.geometry(f"{popup_width}x{popup_height}+{x_position}+{y_position}")

        # Setam culoarea de fundal
        deepface_popup.configure(bg="#000000")

        # Avertizare
        warning_label = tk.Label(deepface_popup,
                                 text="Atentiee: Deepface este mai instabil. \n Doresti sa continui?", padx=20,
                                 pady=10, font=("Helvetica", 12), bg="#000000", fg="#FFFFFF")
        warning_label.pack()

        # Buton pentru a continua
        start_button = tk.Button(deepface_popup, text="Continue", command=self.start_deepface_detection, bg="#4CAF50",
                                 fg="white",
                                 width=7, height=1, font=("Helvetica", 12, "bold"))
        start_button.pack(pady=5)

        # Buton pentru a anula
        cancel_button = tk.Button(deepface_popup, text="Cancel", command=deepface_popup.destroy, bg="#FF4C50",
                                  fg="white",
                                  width=7, height=1, font=("Helvetica", 12, "bold"))
        cancel_button.pack(pady=5)
        

        deepface_popup.mainloop()


def main():
    root = tk.Tk() # Creem o fereastra principala
    app = WarningWindow(root) # Apelam fereastra de atentionare
    root.mainloop() # Afisam fereastra principala

if __name__ == "__main__": # Verificam daca acest script este rulat direct sau importat
    main() # Apelam functia main
