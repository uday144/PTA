package com.ptapp.activity;

/**
 * Created by lifestyle on 12-11-14.
 */
public class TestDummy {
   /*{"students":
       {
           "3":{
               "mycontext":
               {
                "fee":{"Amount":"Rs. 1665","FeeCycle":"April 2014 - March 2015","PaidOn":"10-04-2014","ReceiptNumber":"1003","id":"3"},
                "lstParents":
                    [
                        {"email":"amrishpuri@gmail.com","fName":"Amrish","gender":"Male","id":"3","lName":"Puri","phone":"2323-43343","imgPath":"http://www.tribuneindia.com/2011/20110828/spectrum/main3.jpg"},
                        {"email":"parent2@p.com","fName":"Amrita","gender":"Female","id":"4","lName":"Puri","phone":"2222-32343","imgPath":"http://i.ytimg.com/vi/upSusO2XL2I/hqdefault.jpg"}
                    ],
                "school":{"id":"1","schoolAddress":"Sector 13 -D, Chandigarh","schoolName":"XYZ"},
                "schoolClass":{"attendantId":"25","className":"3rd","clsInchargeEducatorId":"15","feeId":"3","id":"2","section":"B","sessionYear":"2014","transportId":"1"},
                "student":{"address":"#111 Sector 13-A Chandigarh","weight":"60 kg","specialInstructions":"","bloodGroup":"A -ve","doB":"22-02-1999","email":"stu2@gmail.com","fName":"Preeti","foodAllergies":"fod all..2","gender":"Female","height":"4.9 inches","hobbies":"badminton22,cricket2","id":"3","lName":"Kaur","medicalProblems":"medicl prbls..2","medicationAllergies":"med alle...2","medicationNeeds":"needsd...2","otherAllergies":"chalk allergy, rice allergy,Rocking","physicianName":"pju11ewww","physicianPhone":"454444444","rollNumber":"24","specialDietaryNeeds":"spel needs...2","age":7,"annualFamilyIncome":2323002,"imgPath":"http://www.desicomments.com/dc/25/66582/665821.jpg"}
               },
               "age":8
            },
           "2":{
               "mycontext":
               {
                "fee":{"Amount":"Rs. 1665","FeeCycle":"April 2014 - March 2015","PaidOn":"09-04-2014","ReceiptNumber":"1002","id":"2"},
                "lstTags":
                    [
                       {"id":"TYPE_SESSIONS","tagCategory":"TYPE","tagName":"Sessions","tagOrderInCategory":1,"tagColor":"-3355444","tagAbstract":""},
                       {"id":"THEME_DESIGN","tagCategory":"THEME","tagName":"Design","tagOrderInCategory":1,"tagColor":"-3355444","tagAbstract":""},
                       {"id":"THEME_DEVELOP","tagCategory":"THEME","tagName":"Develop","tagOrderInCategory":2,"tagColor":"-3355444","tagAbstract":""},
                       {"id":"TOPIC_MEDIA","tagCategory":"TOPIC","tagName":"Media","tagOrderInCategory":9,"tagColor":"-3259623","tagAbstract":""},
                       {"id":"TOPIC_CLOUDSERVICES","tagCategory":"TOPIC","tagName":"Cloud Services","tagOrderInCategory":3,"tagColor":"-16611119","tagAbstract":""}
                    ],
                "lstCourses":
                    [
                       {"courseName":"English2","educatorId":"9","grade":"","id":"9","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                       {"courseName":"Math2","educatorId":"10","grade":"","id":"10","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                       {"courseName":"Punjabi2","educatorId":"10","grade":"","id":"11","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                       {"courseName":"Hindi2","educatorId":"10","grade":"","id":"12","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                       {"courseName":"French2","educatorId":"10","grade":"","id":"13","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                       {"courseName":"German2","educatorId":"10","grade":"","id":"14","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                       {"courseName":"Science2","educatorId":"15","grade":"","id":"15","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                       {"courseName":"Dutch2","educatorId":"16","grade":"","id":"16","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014}
                    ],
                "lstEducators":
                    [
                       {"email":"e9@e.com","fName":"Edu9","gender":"Male","id":"9","imagePath":"http://saldef.org/wp-content/uploads/2010/01/Romi-282x300.jpg","lName":"Singh","phone":"23111-3433","type":"Educator"},
                       {"email":"jassi.scorpion@gmail.com","fName":"Prof.Jaskirat ","gender":"Female","id":"10","imagePath":"http://www.indiaeducation.net/imagesvr_ce/6228/teacher.jpg","lName":"Kaur","phone":"23111-3433","type":"Educator"},
                       {"email":"e11@e.com","fName":"Edu11","gender":"Male","id":"11","imagePath":"https://www.montana.edu/cpa/news/images/articles/20121214-856550290169856545.jpg","lName":"Singh","phone":"23111-3433","type":"Educator"},
                       {"email":"e12@e.com","fName":"Edu12","gender":"Female","id":"12","imagePath":"http://4.bp.blogspot.com/-JOtVA2H2oSA/UShjYdbhDBI/AAAAAAAAADs/0Pvn24e5dHg/s1600/kulwinder+kaur+punjabi+misstess.jpg","lName":"Kaur","phone":"23111-3433","type":"Educator"},
                       {"email":"e13@e.com","fName":"Edu13","gender":"Male","id":"13","imagePath":"http://3.bp.blogspot.com/-8fQwrWZlgo4/UShjcZ67b_I/AAAAAAAAAFI/opKIFrggD0E/s1600/s.sukhwinder+singh+s.s.+master.jpg","lName":"Singh","phone":"23111-3433","type":"Educator"},
                       {"email":"e14@e.com","fName":"Edu14","gender":"Female","id":"14","imagePath":"http://www.indiaafricaconnect.in/upload/Multimedia/201003281641031_l.jpg","lName":"Kaur","phone":"23111-3433","type":"Educator"},
                       {"email":"e15@e.com","fName":"Edu15","gender":"Male","id":"15","imagePath":"http://1.bp.blogspot.com/-QIOALoAw0ZE/UShjaaNFtbI/AAAAAAAAAEk/xf3gIPTinZU/s1600/s.gurpreet+singh+s.s.+master.jpg","lName":"Singh","phone":"23111-3433","type":"Educator"},
                       {"email":"e16@e.com","fName":"Edu16","gender":"Female","id":"16","imagePath":"http://4.bp.blogspot.com/-r2sFHGGU5y4/UdaYC_SB1KI/AAAAAAAACXc/UF_X6XhLldM/s320/shanti.jpg","lName":"Kaur","phone":"23111-3433","type":"Educator"}
                    ],
                "lstParents":
                   [
                       {"email":"jassi.scorpion@gmail.com","fName":"Jaspreet","gender":"Male","id":"1","lName":"Singh","phone":"2323-43343","imgPath":"http://4.bp.blogspot.com/_87dvm7VjH9U/TKBCjScAxuI/AAAAAAAAA1o/smeYKyN9LQI/s1600/Harveen.jpg"},
                       {"email":"parent2@p.com","fName":"Parent2","gender":"Female","id":"2","lName":"Kaur","phone":"2222-32343","imgPath":"http://3.bp.blogspot.com/_cqR2RKXvhKQ/S6TLuerjSuI/AAAAAAAAAH0/L59wLw94bKY/s400/Snatam_Kaur_1.jpg"}
                   ],
                "school":{"id":"1","schoolAddress":"Sector 13 -D, Chandigarh","schoolName":"XYZ"},
                "schoolClass":{"attendantId":"25","className":"3rd","clsInchargeEducatorId":"15","feeId":"2","id":"2","section":"B","sessionYear":"2014","transportId":"1"},
                "student":{"address":"#111 Sector 13-A Chandigarh","weight":"60 kg","specialInstructions":"","bloodGroup":"A -ve","doB":"22-02-1999","email":"stu2@gmail.com","fName":"Sukhnain","foodAllergies":"fod all..2","gender":"Female","height":"4.9 inches","hobbies":"badminton22, cricket2","id":"2","lName":"Kaur","medicalProblems":"medicl prbls..2","medicationAllergies":"med alle...2","medicationNeeds":"needsd...2","otherAllergies":"chalk allergy, rice allergy, Rocking","physicianName":"pju11ewww","physicianPhone":"454444444","rollNumber":"24","specialDietaryNeeds":"spel needs...2","age":7,"annualFamilyIncome":2323002,"imgPath":"http://pastmist.files.wordpress.com/2009/02/sikh-girl-from-bangalore.jpg?w=458&h=345"},
                "transport":{"ConductorFName":"Shorey","ConductorLName":"Martandey","ConductorPhoneNumber":"912389342","DriverFName":"Kapil","DriverLName":"Dubey","DriverPhoneNumber":"9134533422","RouteName":"weres route","SPOCPhoneNumber":"02-2324-3422","StopName":"dixxt stop","VehicleNumber":"CH-03-DI-4322","ZoneName":"this zone","id":"2","way":"This is second way"}
               },
               "age":0
            },
           "1":{
               "mycontext":
                {
                 "fee":{"Amount":"Rs. 1500","FeeCycle":"April 2014 - March 2015","PaidOn":"09-04-2014","ReceiptNumber":"1001","id":"1"},
                 "lstCourses":
                    [
                        {"courseName":"English","educatorId":"1","grade":"","id":"1","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                        {"courseName":"Math","educatorId":"2","grade":"","id":"2","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                        {"courseName":"Punjabi","educatorId":"3","grade":"","id":"3","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                        {"courseName":"Hindi","educatorId":"4","grade":"","id":"4","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                        {"courseName":"French","educatorId":"5","grade":"","id":"5","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                        {"courseName":"German","educatorId":"6","grade":"","id":"6","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                        {"courseName":"Science","educatorId":"7","grade":"","id":"7","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014},
                        {"courseName":"Dutch","educatorId":"8","grade":"","id":"8","unit1Score":0,"unit2Score":0,"unit3Score":0,"year":2014}
                    ],
                 "lstEducators":
                    [
                        {"email":"jas.scorpion@gmail.com","fName":"Jasraj","gender":"Male","id":"1","imagePath":"http://www.indianschool.bh/photos/administrators/t-mani.jpg","lName":"Singh","phone":"23111-3433","type":"Educator"},
                        {"email":"e2@e.com","fName":"Edu2","gender":"Female","id":"2","imagePath":"http://1-ps.googleusercontent.com/h/www.careerindia.com/img/2014/08/18-essaywriting.jpg.pagespeed.ce.fO8GK33B5b.jpg","lName":"Kaur","phone":"23111-3433","type":"Educator"},
                        {"email":"e3@e.com","fName":"Edu3","gender":"Male","id":"3","imagePath":"http://www.indianschool.bh/photos/administrators/Pillai.jpg","lName":"Singh","phone":"23111-3433","type":"Educator"},
                        {"email":"e4@e.com","fName":"Edu4","gender":"Female","id":"4","imagePath":"https://dominicanteachforindia.files.wordpress.com/2012/08/teach-for-india-074.jpg","lName":"Kaur","phone":"23111-3433","type":"Educator"},
                        {"email":"e5@e.com","fName":"Edu5","gender":"Male","id":"5","imagePath":"http://www.education.umd.edu/international/CurrentInitiatives/scholars/FDT%20Images/IMG_1611.jpg","lName":"Singh","phone":"23111-3433","type":"Educator"},
                        {"email":"e6@e.com","fName":"Edu6","gender":"Female","id":"6","imagePath":"http://www.education.umd.edu/international/CurrentInitiatives/scholars/FDT%20Images/IMG_1562.jpg","lName":"Kaur","phone":"23111-3433","type":"Educator"},
                        {"email":"e7@e.com","fName":"Edu7","gender":"Male","id":"7","imagePath":"http://3.bp.blogspot.com/-JQfLmZWJn_s/UShjWAskX0I/AAAAAAAAADI/YxSAmLDJYWI/s1600/Harwinder+singh+computer+teacher.jpg","lName":"Singh","phone":"23111-3433","type":"Educator"},
                        {"email":"e8@e.com","fName":"Edu8","gender":"Female","id":"8","imagePath":"https://s3-eu-west-1.amazonaws.com/tutors.firsttutors.com/19/18229/med.jpg","lName":"Kaur","phone":"23111-3433","type":"Educator"}
                    ],
                 "lstParents":
                    [
                        {"email":"jassi.scorpion@gmail.com","fName":"Jaspreet","gender":"Male","id":"1","lName":"Singh","phone":"2323-43343","imgPath":"http://4.bp.blogspot.com/_87dvm7VjH9U/TKBCjScAxuI/AAAAAAAAA1o/smeYKyN9LQI/s1600/Harveen.jpg"},
                        {"email":"parent2@p.com","fName":"Parent2","gender":"Female","id":"2","lName":"Kaur","phone":"2222-32343","imgPath":"http://3.bp.blogspot.com/_cqR2RKXvhKQ/S6TLuerjSuI/AAAAAAAAAH0/L59wLw94bKY/s400/Snatam_Kaur_1.jpg"}
                    ],
                 "school":{"id":"1","schoolAddress":"Sector 13 -D, Chandigarh","schoolName":"XYZ"},
                 "schoolClass":{"attendantId":"22","className":"2nd","clsInchargeEducatorId":"1","feeId":"1","id":"1","section":"A","sessionYear":"2014","transportId":"1"},
                 "student":{"address":"#332 Sector 13-A Chandigarh","weight":"55 kg","specialInstructions":"","bloodGroup":"B +ve","doB":"22-02-2000","email":"stu1@gmail.com","fName":"Diljit","foodAllergies":"fod all..1","gender":"Male","height":"5.5 inches","hobbies":"badminton, cricket","id":"1","lName":"Singh","medicalProblems":"medicl prbls..1","medicationAllergies":"med alle...1","medicationNeeds":"needsd...1","otherAllergies":"dust allergy, wheat allergy, aller3","physicianName":"pju11","physicianPhone":"454444444","rollNumber":"23","specialDietaryNeeds":"spel needs...1","age":5,"annualFamilyIncome":2323002,"imgPath":"http://sphotos-b.ak.fbcdn.net/hphotos-ak-xfa1/t1.0-9/574748_10151053922673940_686321950_n.jpg"},
                 "transport":{"ConductorFName":"shammu","ConductorLName":"Yadav","ConductorPhoneNumber":"9144323221","DriverFName":"Rammu","DriverLName":"Yadav","DriverPhoneNumber":"91323323232","RouteName":"xyz route","SPOCPhoneNumber":"02-2324-3422","StopName":"st stop","VehicleNumber":"CH-04-PX-2332","ZoneName":"zzz zone","id":"1","way":"This is one way"}
                },
           "age":0
            }
       }
   }*/
}
