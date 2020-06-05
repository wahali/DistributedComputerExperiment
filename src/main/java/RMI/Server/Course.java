package RMI.Server;

public class Course {

    String name;//课程名称

    double grading;//课程绩点

    public Course(){

        grading=4.0;

        name="计算机导论";

    }

    public String getName() {

        return name;

    }

    public double getGrading() {

        return grading;

    }

}