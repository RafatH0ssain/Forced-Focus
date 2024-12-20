import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;

public class WebcamCapture {
    public static void main(String[] args) {
        // Load the OpenCV library
        System.load("D:/projects/opencv/build/java/x64/opencv_java500.dll"); // Update path

        // Initialize the VideoCapture object (camera ID 0 is default)
        VideoCapture camera = new VideoCapture(0);

        // Check if the camera is opened
        if (!camera.isOpened()) {
            System.out.println("Error: Camera is not accessible");
            return;
        }

        // Create a Mat object to store the captured frame
        Mat frame = new Mat();

        // Start capturing frames from the webcam
        while (true) {
            // Capture a new frame
            camera.read(frame);

            // Display the frame using HighGui (OpenCV's built-in GUI functions)
            HighGui.imshow("Webcam", frame);

            // Exit loop on pressing 'ESC'
            if (HighGui.waitKey(1) == 27) {
                break;
            }
        }

        // Release resources
        camera.release();
        HighGui.destroyAllWindows();
    }
}
