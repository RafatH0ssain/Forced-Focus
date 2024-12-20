import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Point;

public class EyeTracking {
    public static void main(String[] args) {
        System.load("D:/projects/opencv/build/java/x64/opencv_java4100.dll");
        System.out.println("OpenCV Version: " + Core.getVersionString());

        // Initialize the webcam
        VideoCapture capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Error: Webcam not detected.");
            return;
        }

        // Load the face and eye detection classifiers
        CascadeClassifier faceCascade = new CascadeClassifier("xml files/haarcascade_frontalface_alt2.xml");
        CascadeClassifier eyeCascade = new CascadeClassifier("xml files/haarcascade_eye.xml");

        // Initialize the Mat object for storing frames
        Mat frame = new Mat();

        while (true) {
            capture.read(frame);  // Capture the current frame
            if (frame.empty()) {
                System.out.println("Error: No frame captured.");
                break;
            }

            // Convert the frame to grayscale for better detection performance
            Mat gray = new Mat();
            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

            // Detect faces
            MatOfRect facesMat = new MatOfRect();  // Use MatOfRect for face detection
            faceCascade.detectMultiScale(gray, facesMat, 1.1, 2, 0, new Size(30, 30), new Size(300, 300));

            // Convert MatOfRect to an array of Rect
            Rect[] faces = facesMat.toArray();

            // For each detected face, try to detect eyes
            for (Rect face : faces) {
                Imgproc.rectangle(frame, face.tl(), face.br(), new Scalar(0, 255, 0), 2);  // Draw rectangle around the face

                // Detect eyes in the region of interest (ROI) for each face
                Mat faceROI = gray.submat(face);
                MatOfRect eyesMat = new MatOfRect();  // Use MatOfRect for eye detection
                eyeCascade.detectMultiScale(faceROI, eyesMat);

                // Convert MatOfRect to an array of Rect for eyes
                Rect[] eyes = eyesMat.toArray();

                for (Rect eye : eyes) {
                    Imgproc.rectangle(frame, new Point(face.x + eye.x, face.y + eye.y),
                            new Point(face.x + eye.x + eye.width, face.y + eye.y + eye.height),
                            new Scalar(255, 0, 0), 2);  // Draw rectangle around the eyes
                }
            }

            // Display the frame with face and eye detection
            HighGui.imshow("Webcam Feed", frame);  // Display the captured frame

            // Wait for 1 ms to check if the user presses the ESC key
            int key = HighGui.waitKey(1);
            if (key == 27) {  // ESC key to exit the loop
                break;
            }
        }


        // Release the webcam and close any open windows
        capture.release();
        HighGui.destroyAllWindows();
    }
}
