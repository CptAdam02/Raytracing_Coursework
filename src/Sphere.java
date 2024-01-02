public class Sphere {

    Vector centre;

    double radius;
    colour colour;


    public Sphere(Vector centre, double radius, colour colour) {
        this.centre = centre;
        this.radius = radius;
        this.colour = colour;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "centre=" + centre +
                ", radius=" + radius +
                ", colour=" + colour +
                '}';
    }

    public Vector getCenter() {
        return centre;
    }

    public void setCenter(Vector center) {
        this.centre = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public colour getColour() {
        return colour;
    }

    public void setColour(colour colour) {
        this.colour = colour;
    }

    public double intersection(Vector o, Sphere sphere, Vector d, Vector Light ) {
        double col = 0.0;
        double a, b, c;
        Vector v;
        v = o.sub(sphere.getCenter());
        a = d.dot(d);
        b = 2 * v.dot(d);
        c = v.dot(v) - sphere.getRadius() * sphere.getRadius();
        double disc = b * b - 4 * a * c;
        double t = (-b - Math.sqrt(disc)) / 2 * a;     //this tells us which one is the closest
        Vector p = (d.mul(t)).add(o);
        Vector Lv = Light.sub(p);
        Lv.normalise();
        Vector n = p.sub(sphere.getCenter());
        n.normalise();
        double dp = Lv.dot(n);
        if (dp < 0) col = 0.0;
        else col = dp;
        if (col > 1) col = 1;
        return col;
    }

    public double distance(Vector o, Sphere sphere, Vector d, Vector Light ) {
        double a, b, c;
        Vector v;
        v = o.sub(sphere.getCenter());
        a = d.dot(d);
        b = 2 * v.dot(d);
        c = v.dot(v) - sphere.getRadius() * sphere.getRadius();
        double disc = b * b - 4 * a * c;
        return (-b - Math.sqrt(disc)) / 2 * a;
    }

    public boolean intersectCheck(Vector o, Sphere sphere, Vector d, Vector Light ) {
        boolean check = false;
        double a, b, c;
        Vector v;
        v = o.sub(sphere.getCenter());
        a = d.dot(d);
        b = 2 * v.dot(d);
        c = v.dot(v) - sphere.getRadius() * sphere.getRadius();
        double disc = b * b - 4 * a * c;
        if (disc >= 1 ){
            check = true;
        }
        return check;
    }

}
