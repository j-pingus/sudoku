
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Point;

import static org.opencv.core.CvType.CV_8UC1;

public class Main {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    public static void main(String[] args) {
        //TODO add path to your file for the moment
        new SudokuGrabber("C:\\Users\\aliafa\\Downloads\\new\\sudoku-original.jpg").grab();
    }

    public static class SudokuGrabber {

        private String imagePath;

        public SudokuGrabber(String imagePath) {
            this.imagePath = imagePath;
        }

        public void grab() {
            //load image
            Mat sudoku = Imgcodecs.imread(imagePath, CV_8UC1);

            //blur the image
            Mat outerBox = new Mat(sudoku.size(), CV_8UC1);
            Imgproc.GaussianBlur(sudoku, sudoku, new Size(11, 11), 0);

            //threshold the image
            Imgproc.adaptiveThreshold(sudoku, outerBox, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 2);

            //invert the image
            Core.bitwise_not(outerBox, outerBox);

            //dilate the image
            Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
            Imgproc.dilate(outerBox, outerBox, element);

            //find the biggest blob
            new BlobHelper(outerBox).findBiggestBlob();

            //erode the image
            Imgproc.erode(outerBox, outerBox, element);

            new LineHelper(outerBox).houghLines();

            //debug
            Imgcodecs.imwrite("C:\\Users\\aliafa\\Downloads\\new\\sudoku-res.jpg", outerBox);
        }
    }

    public static class LineHelper {
        private Mat outerBox;

        private Mat lines = new Mat();

        public LineHelper(Mat outerBox) {
            this.outerBox = outerBox;
        }

        public void houghLines() {
            //get houghlines
            Imgproc.HoughLines(outerBox, lines, 1, Math.PI / 180, 200);

            drawLines();
        }

        private void drawLines() {
            //draw lines
            //this helped http://stackoverflow.com/questions/29872439/opencv-houghlines-only-ever-returning-one-line

            for (int i = 0; i < lines.rows(); i++) {
                double[] line = lines.get(i, 0);

                if (line[1] != 0) {
                    double m = -1 / Math.tan(line[1]);
                    double c = line[0] / Math.sin(line[1]);
                    Imgproc.line(outerBox, new Point(0, c), new Point(outerBox.size().width, m * outerBox.size().width + c), new Scalar(128, 0, 0));
                } else {
                    Imgproc.line(outerBox, new Point(line[0], 0), new Point(line[0], outerBox.size().height), new Scalar(128, 0, 0));
                }
            }
        }

        private void mergeLines() {
            for (int i = 0; i < lines.rows(); i++) {
                double[] line = lines.get(i, 0);

                if (line[0] == 0 && line[1] == -100) continue;

                double p1 = line[0];
                double theta1 = line[1];

                Point pt1current = new Point();
                Point pt2current = new Point();

                if (theta1 > Math.PI * (45 / 180) && theta1 < Math.PI * (135 / 180)) {
                    pt1current.x = 0;
                    pt1current.y = p1 / Math.sin(theta1);

                    pt2current.x = outerBox.size().width;
                    pt2current.y = -pt2current.x / (Math.tan(theta1) + (p1 / Math.sin(theta1)));
                } else {
                    pt1current.y = 0;
                    pt1current.x = p1 / Math.cos(theta1);

                    pt2current.y = outerBox.size().height;
                    pt2current.x = -pt2current.y / (Math.tan(theta1) + (p1 / Math.cos(theta1)));

                }

                //compare every line with every other line
                for (int j = 0; j < lines.rows(); j++) {
                    double[] posLine = lines.get(j, 0);

                    //skip if same lines
                    if ((line[0] == posLine[0]) && (line[1]) == posLine[1]) continue;

                    //TODO compare lines

                }

            }
        }
    }


    public static class BlobHelper {
        private Mat outerBox;

        private Point maxPt;

        private int maxArea = -1;

        public BlobHelper(Mat outerBox) {
            this.outerBox = outerBox;
        }

        public void findBiggestBlob() {
            floodFillWhitePartsToGray();
            floodMaxAreaToWhite();
            floodFillOtherBlobsToBlack();
        }

        private void floodFillWhitePartsToGray() {
            for (int y = 0; y < outerBox.size().height; y++) {
                for (int x = 0; x < outerBox.size().width; x++) {
                    if (outerBox.get(y, x)[0] >= 128) {
                        int area = Imgproc.floodFill(outerBox, newStub(), new Point(x, y), new Scalar(64, 0, 0));

                        if (area > maxArea) {
                            maxPt = new Point(x, y);
                            maxArea = area;
                        }
                    }
                }
            }
        }

        private void floodMaxAreaToWhite() {
            Imgproc.floodFill(outerBox, newStub(), maxPt, new Scalar(255, 255, 255));
        }

        private void floodFillOtherBlobsToBlack() {
            for (int y = 0; y < outerBox.size().height; y++) {
                for (int x = 0; x < outerBox.size().width; x++) {
                    if (outerBox.get(y, x)[0] == 64 && x != maxPt.x && y != maxPt.y) {
                        Imgproc.floodFill(outerBox, newStub(), new Point(x, y), new Scalar(0, 0, 0));
                    }
                }
            }
        }

        private Mat newStub() {
            return new Mat(outerBox.rows() + 2, outerBox.cols() + 2, CvType.CV_8U);
        }
    }
}
