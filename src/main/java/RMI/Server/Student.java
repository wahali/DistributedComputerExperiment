package RMI.Server;

import RMI.Server.Course;
import RMI.Server.Person;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Student extends Person implements RemoteService{

    Course course;
    private static  final int REGISTRY_PORT = 9909;
    private static  final String REMOTE_HOST = "localhost";
    private static  final String SERVICE_URL = "rmi://" + REMOTE_HOST + ":" + REGISTRY_PORT;
    private static  final String SERVICE_NAME = "user";
    public Student() throws RemoteException {

        this.age=20;

        this.name="张三";

        course=new Course();

    }

    public String inquiry(String n){

        String inquiryResult=null;

        if(n.equals("张三")){

            inquiryResult=this.name+"年龄为"+age;

            inquiryResult=inquiryResult+"选修课程为"+course.getName();

            inquiryResult=inquiryResult+"课程绩点为"+course.getGrading();

        }

        else {
            inquiryResult="找不到该人的信息！";
        }

        return inquiryResult;

    }

}