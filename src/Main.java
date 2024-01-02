/*
CS-255 Getting started code for the assignment
I do not give you permission to post this code online
Do not post your solution online
Do not copy code
Do not use JavaFX functions or other libraries to do the main parts of the assignment (i.e. ray tracing steps 1-7)
All of those functions must be written by yourself
You may use libraries to achieve a better GUI
*/
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;


public class Main extends Application {
  int Width = 1600;
  int Height = 700;

  Vector Light = new Vector(0,0,-400);

  Vector o = new Vector(0,0,0);                //origin of the ray
  Vector d = new Vector(0,0,1);                //direction of the ray

  int green_col = 255; //just for the test example

  char colourSelect = 'r';
  char positionSelect = 'x';
  int sphereSelect = 0;


  //Positions of the spheres
  Vector cs1 = new Vector(0,0,200);
  Vector cs2 = new Vector(0,50,200);
  Vector cs3 = new Vector(400,-200,200);
  Vector cs4 = new Vector(-400,200,200);

  //The spheres themselves 
  Sphere sphere1 = new Sphere (cs1,50, new colour(0,1,0));
  Sphere sphere2 = new Sphere (cs2,50 , new colour(1, 0, 0 ));
  Sphere sphere3 = new Sphere (cs3,50 , new colour(0, 0, 1 ));
  Sphere sphere4 = new Sphere (cs4,50 , new colour(1, 0, 1 ));

  ArrayList<Sphere> sphereList = new ArrayList<>();

  Slider r_slider = new Slider(0, 255, green_col);
  Slider c_slider = new Slider(0, 255, green_col);
  Slider p_slider = new Slider(0, 255, green_col);

  @Override
  public void start(Stage stage) {
    stage.setTitle("Ray Tracing");

    sphereList.add(sphere1);
    sphereList.add(sphere2);
    sphereList.add(sphere3);
    sphereList.add(sphere4);

    //Image Pane
    WritableImage image = new WritableImage(Width, Height);
    ImageView view = new ImageView(image);

    //random color button
    Button colour_button = new Button("Disco mode");
    EventHandler<ActionEvent> buttonClick = e -> {
      for (Sphere sphere : sphereList) {
        Random rand = new Random();
        sphere.setColour(new colour (rand.nextDouble(),rand.nextDouble(),rand.nextDouble()));
        sliderUpdate();
        Render(image);
      }
    };
    colour_button.setOnAction(buttonClick);

    Label Explentation = new Label("Top Bar is radius. Middle bar is colour. Bottom bar is position.");

    r_slider.setShowTickLabels(true);
    r_slider.setShowTickMarks(true);
    r_slider.setMajorTickUnit(100);
    r_slider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
              Sphere sphere = sphereList.get(sphereSelect);
              double newRadius = observable.getValue().doubleValue();
              sphere.setRadius(newRadius);
              Render(image);
            }
    );

    //colour slider
    c_slider.setShowTickLabels(true);
    c_slider.setShowTickMarks(true);
    c_slider.setMajorTickUnit(100);
    c_slider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
              Sphere sphere = sphereList.get(sphereSelect);

              double newColour = observable.getValue().doubleValue() /255;
              if (colourSelect == 'r'){
                sphere.getColour().setR(newColour);
              }else if (colourSelect == 'g'){
                sphere.getColour().setG(newColour);
              }else if (colourSelect == 'b'){
                sphere.getColour().setB(newColour);
              }
              Render(image);
            }
    );

    //position slider
    p_slider.setShowTickLabels(true);
    p_slider.setShowTickMarks(true);
    p_slider.setMajorTickUnit(100);
    p_slider.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
              double newPosition;
              if (sphereSelect == 4){

                if (positionSelect == 'x'){
                  newPosition = ((observable.getValue().doubleValue()/255)*1600)-800;
                  Light.setX(newPosition);
                }else if (positionSelect == 'y'){
                  newPosition = ((observable.getValue().doubleValue()/255)*700)-350;
                  Light.setY(newPosition);
                }else if (positionSelect == 'z') {
                  newPosition = ((observable.getValue().doubleValue()/255)*1600)-800;
                  Light.setZ(newPosition);
                }
                Render(image);
              }else if (sphereSelect == 5){
                if (positionSelect == 'x'){
                  newPosition = ((observable.getValue().doubleValue()/255)*1600)-800;
                  o.setX(newPosition);
                }else if (positionSelect == 'y') {
                  newPosition = ((observable.getValue().doubleValue() / 255) * 700) - 350;
                  o.setY(newPosition);
                }
                Render(image);

              }else{
                Sphere sphere = sphereList.get(sphereSelect);
                if (positionSelect == 'x'){
                  newPosition = ((observable.getValue().doubleValue()/255)*1600)-800;
                  sphere.getCenter().setX(newPosition);
                }else if (positionSelect == 'y'){
                  newPosition = ((observable.getValue().doubleValue()/255)*700)-350;
                  sphere.getCenter().setY(newPosition);
                }else if (positionSelect == 'z') {
                  newPosition = ((observable.getValue().doubleValue() / 255) * 400) - 200;
                  sphere.getCenter().setZ(newPosition);
                }
                Render(image);
              }
            }
    );

    //Colour toggle group
    Label colour_label = new Label("Colour");
    ToggleGroup colTG = new ToggleGroup();
    RadioButton rRed = new RadioButton("red");
    RadioButton rGreen = new RadioButton("green");
    RadioButton rBlue = new RadioButton("blue");
    rRed.setToggleGroup(colTG);
    rGreen.setToggleGroup(colTG);
    rBlue.setToggleGroup(colTG);
    colTG.selectToggle(rRed);
    colTG.selectedToggleProperty().addListener((ob, o, n) -> {
      RadioButton rb1 = (RadioButton)colTG.getSelectedToggle();
      if (rb1 != null) {
        String s = rb1.getText();
        colourSelect = s.charAt(0);
        sliderUpdate();
        System.out.println(colourSelect);
      }
    });

    //Movement toggle group
    Label dir_label = new Label("Direction");
    ToggleGroup dirTG = new ToggleGroup();
    RadioButton rX = new RadioButton(" x ");
    RadioButton rY= new RadioButton(" y ");
    RadioButton rZ= new RadioButton(" z ");
    rX.setToggleGroup(dirTG);
    rY.setToggleGroup(dirTG);
    rZ.setToggleGroup(dirTG);
    dirTG.selectToggle(rX);
    dirTG.selectedToggleProperty().addListener((ob, o, n) -> {
      RadioButton rb2 = (RadioButton)dirTG.getSelectedToggle();
      if (rb2 != null) {
        String s = rb2.getText();
        positionSelect = s.charAt(1);
        sliderUpdate();
        System.out.println(positionSelect);
      }
    });

    //Circle control group
    Label cir_label = new Label("Circle");
    ToggleGroup cirTG = new ToggleGroup();
    RadioButton rOne = new RadioButton("One");
    RadioButton rTwo= new RadioButton("Two");
    RadioButton rThree= new RadioButton("Three");
    RadioButton rFour= new RadioButton("Four");
    RadioButton rLight= new RadioButton("Light");
    RadioButton rCam= new RadioButton("Cam");
    rOne.setToggleGroup(cirTG);
    rTwo.setToggleGroup(cirTG);
    rThree.setToggleGroup(cirTG);
    rFour.setToggleGroup(cirTG);
    rLight.setToggleGroup(cirTG);
    rCam.setToggleGroup(cirTG);
    cirTG.selectToggle(rOne);
    cirTG.selectedToggleProperty().addListener((ob, o, n) -> {
      RadioButton rb3 = (RadioButton)cirTG.getSelectedToggle();
      if (rb3 != null) {
        String s = rb3.getText();
        if (Objects.equals(s, "One")){
          sphereSelect = 0;
          sliderUpdate();
        }else if (Objects.equals(s, "Two")){
          sphereSelect = 1;
          sliderUpdate();
        }else if (Objects.equals(s, "Three")){
          sphereSelect = 2;
          sliderUpdate();
        }else if (Objects.equals(s, "Four")) {
          sphereSelect = 3;
          sliderUpdate();
        }else if (Objects.equals(s, "Light")) {
          sphereSelect = 4;
          sliderUpdate();
        }else if (Objects.equals(s, "Cam")) {
          sphereSelect = 5;
          sliderUpdate();
        }

      }
    });

    Label cam_label = new Label("Camera");
    ToggleGroup camTG = new ToggleGroup();
    RadioButton rFor = new RadioButton("Forward");
    RadioButton rBac = new RadioButton("Back");
    rFor.setToggleGroup(camTG);
    rBac.setToggleGroup(camTG);
    camTG.selectToggle(rFor);
    camTG.selectedToggleProperty().addListener((ob, o, n) -> {
      RadioButton rb4 = (RadioButton)camTG.getSelectedToggle();
      if (rb4 != null) {
        String s = rb4.getText();
        if (Objects.equals(s, "Forward")){
          d.setX(0);
          d.setY(0);
          d.setZ(1);
          Render(image);
        }else if (Objects.equals(s, "Back")){
          d.setX(0);
          d.setY(0);
          d.setZ(-1);
          Render(image);
        }
      }
    });

    //The following is in case you want to interact with the image in any way
    //e.g., for user interaction, or you can find out the pixel position for debugging
    view.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
      System.out.println(event.getX() + " " + event.getY());
      event.consume();
    });

    sliderUpdate();
    Render(image);


    GridPane root = new GridPane();
    root.setVgap(12);
    root.setHgap(12);

    //3. (referring to the 3 things we need to display an image)
    //we need to add it to the pane
    
    root.add(view, 0, 0);

    root.add(r_slider, 0, 1);
    root.add(c_slider, 0, 2);
    root.add(p_slider, 0, 3);
    root.add(Explentation, 0, 4);


    root.add(colour_button,1,0);

    root.add(colour_label,1,1);
    root.add(rRed,1,2);
    root.add(rGreen,1,3);
    root.add(rBlue,1,4);

    root.add(dir_label ,2,1);
    root.add(rX,2,2);
    root.add(rY,2,3);
    root.add(rZ,2,4);

    root.add(cir_label,3,1);
    root.add(rOne,3,2);
    root.add(rTwo,3,3);
    root.add(rThree,3,4);
    root.add(rFour,3,5);
    root.add(rLight,3,6);
    root.add(rCam,3,7);

    root.add(cam_label,4,1);
    root.add(rFor,4,2);
    root.add(rBac,4,3);
    
    //Display to user
    Scene scene = new Scene(root, 1920, 1080);
    stage.setScene(scene);
    stage.show();
  }

  public void sliderUpdate(){
    double value = 0;
    if (sphereSelect == 4){
      if (positionSelect == 'x'){
        value = ((Light.getX()+800)/1600)*255;
      } else if (positionSelect == 'y'){
        value = ((Light.getY()+350)/700)*255;
      } else if (positionSelect == 'z'){
        value = ((Light.getZ()+800)/1600)*255;
      }
      p_slider.setValue(value);
    }else if(sphereSelect == 5){
      if (positionSelect == 'x'){
        value = ((o.getX()+800)/1600)*255;
      } else if (positionSelect == 'y'){
        value = ((o.getY()+350)/700)*255;
      }
      p_slider.setValue(value);
    }else{
      Sphere sphere = sphereList.get(sphereSelect);

      r_slider.setValue(sphere.getRadius());
      if (positionSelect == 'x'){
        value = ((sphere.getCenter().getX()+800)/1600)*255;
      } else if (positionSelect == 'y'){
        value = ((sphere.getCenter().getY()+350)/700)*255;
      } else if (positionSelect == 'z'){
        value = ((sphere.getCenter().getZ()+800)/1600)*255;
      }
      p_slider.setValue(value);
      if (colourSelect == 'r'){
        value = sphere.getColour().getR()*255;
      } else if (colourSelect == 'g'){
        value = sphere.getColour().getG()*255;
      } else if (colourSelect == 'b'){
        value = sphere.getColour().getB()*255;
      }
      c_slider.setValue(value);
    }
  }

  public void Render(WritableImage image) {
    //Get image dimensions, and declare loop variables
    int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j;
    PixelWriter image_writer = image.getPixelWriter();
    double col;

    for (j = 0; j < h; j++) {
      for (i = 0; i < w; i++) {
        double oldX = o.getX();
        double oldY = o.getY();

        o.x = i - 800 + o.getX() ;
        o.y = j - 350 + o.getY() ;
        o.z =  -200;



        double r = 0.0;
        double g = 0.0;
        double b = 0.0;

        int checkCount = 0;

        if(sphere1.intersectCheck(o,sphere1,d,Light)){checkCount = checkCount + 1;}
        if(sphere2.intersectCheck(o,sphere2,d,Light)){checkCount = checkCount + 1;}
        if(sphere3.intersectCheck(o,sphere3,d,Light)){checkCount = checkCount + 1;}
        if(sphere4.intersectCheck(o,sphere4,d,Light)){checkCount = checkCount + 1;}

        if (checkCount > 1){
          double oldDist = 0.0;
          int closeSphere = 0;

          for (int k = 0;k<sphereList.size();k++){
            Sphere sphere = sphereList.get(k);
            double dist = sphere.distance(o,sphere,d,Light);
            if (dist<oldDist){
              closeSphere = k;
            }
          }

          Sphere sphere = sphereList.get(closeSphere);
          col = sphere.intersection(o,sphere,d,Light);
          r = sphere.getColour().getR()*col;
          g = sphere.getColour().getG()*col;
          b = sphere.getColour().getB()*col;

        }else{
          if (sphere1.intersectCheck(o,sphere1,d,Light)){
            col = sphere1.intersection(o,sphere1,d,Light);
            r = sphere1.getColour().getR()*col;
            g = sphere1.getColour().getG()*col;
            b = sphere1.getColour().getB()*col;
          }else if (sphere2.intersectCheck(o,sphere2,d,Light)){
            col = sphere2.intersection(o,sphere2,d,Light);
            r = sphere2.getColour().getR()*col;
            g = sphere2.getColour().getG()*col;
            b = sphere2.getColour().getB()*col;
          }else if (sphere3.intersectCheck(o,sphere3,d,Light)){
            col = sphere3.intersection(o,sphere3,d,Light);
            r = sphere3.getColour().getR()*col;
            g = sphere3.getColour().getG()*col;
            b = sphere3.getColour().getB()*col;
          }else if (sphere4.intersectCheck(o,sphere4,d,Light)) {
            col = sphere4.intersection(o, sphere4, d, Light);
            r = sphere4.getColour().getR() * col;
            g = sphere4.getColour().getG() * col;
            b = sphere4.getColour().getB() * col;
          }
        }
        image_writer.setColor(i, j, Color.color(r, g, b, 1.0));

        o.setX(oldX);
        o.setY(oldY);
      } // column loop
    } // row loop

  }

  public static void main(String[] args) {
    launch();
  }

}