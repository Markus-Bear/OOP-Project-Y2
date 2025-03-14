package controller;

/**
 * Utility class for handling various methods for user, equipment, and reservation management.
 */
public class MethodsUtil {

    // Return the full list of departments.
    public static String[] getDepartments() {
        return new String[]{
                "Sport Management", "Architecture", "Arts & Social Studies", "Built Environment", "Business",
                "Computing", "Engineering", "Hospitality, Tourism & Culinary Arts", "Early Years Education", "Media",
                "Design & Music", "Nursing", "Health & Psychology", "Science", "Law", "Sport Science"
        };
    }


    /**
     * Returns an array of courses based on the provided department.
     *
     * @param department the department.
     * @return an array of available courses.
     */
    public static String[] getCoursesForDepartment(String department) {
        switch (department) {
            case "Sport Management":
                return new String[]{
                        "Sport Business Management",
                        "Sports Development and Event Management",
                        "Sport Marketing and Sponsorship",
                        "Sports Facility and Venue Management",
                        "Global Sport Governance"
                };
            case "Architecture":
                return new String[]{
                        "Architectural Technology",
                        "Environmental Design",
                        "Urban Planning and Design",
                        "Sustainable Building Design",
                        "Construction and Architectural Engineering"
                };
            case "Arts & Social Studies":
                return new String[]{
                        "Sociology",
                        "Cultural Studies",
                        "Fine Arts: Painting and Drawing",
                        "Performing Arts: Acting and Theatre",
                        "Psychology in Society"
                };
            case "Built Environment":
                return new String[]{
                        "Construction Management",
                        "Quantity Surveying",
                        "Real Estate and Property Development",
                        "Facilities Management",
                        "Building Information Modelling (BIM)"
                };
            case "Business":
                return new String[]{
                        "Business Administration",
                        "Marketing Strategy",
                        "Finance and Investment",
                        "Human Resource Management",
                        "International Trade and Commerce"
                };
            case "Computing":
                return new String[]{
                        "Software Development",
                        "Cybersecurity and Networking",
                        "Data Science and Analytics",
                        "Artificial Intelligence and Machine Learning",
                        "Web and Mobile Application Development"
                };
            case "Engineering":
                return new String[]{
                        "Mechanical Engineering",
                        "Electrical and Electronic Engineering",
                        "Civil Engineering",
                        "Manufacturing and Production Engineering",
                        "Renewable Energy Systems"
                };
            case "Hospitality, Tourism & Culinary Arts":
                return new String[]{
                        "Hotel and Resort Management",
                        "Tourism Destination Management",
                        "Event Planning and Coordination",
                        "Culinary Arts and Gastronomy",
                        "Wine and Beverage Management"
                };
            case "Early Years Education":
                return new String[]{
                        "Early Childhood Care and Education",
                        "Child Development and Psychology",
                        "Inclusive Practices in Early Years",
                        "Play and Learning in Early Childhood",
                        "Leadership in Early Years Settings"
                };
            case "Media":
                return new String[]{
                        "Journalism and News Writing",
                        "Film and Video Production",
                        "Digital Media and Social Media Management",
                        "Public Relations and Advertising",
                        "Broadcast Media and Television Production"
                };
            case "Design & Music":
                return new String[]{
                        "Graphic Design and Visual Communication",
                        "Interior Design and Space Planning",
                        "Music Production and Sound Engineering",
                        "Fashion Design and Textiles",
                        "Game Design and Animation"
                };
            case "Nursing":
                return new String[]{
                        "General Nursing",
                        "Midwifery",
                        "Community Health Nursing",
                        "Mental Health Nursing",
                        "Pediatric Nursing"
                };
            case "Health & Psychology":
                return new String[]{
                        "Health Sciences",
                        "Occupational Therapy",
                        "Clinical Psychology",
                        "Counseling and Psychotherapy",
                        "Nutrition and Dietetics"
                };
            case "Science":
                return new String[]{
                        "Biology",
                        "Chemistry",
                        "Physics",
                        "Environmental Science",
                        "Biotechnology and Genetics"
                };
            case "Law":
                return new String[]{
                        "Legal Studies",
                        "Business Law",
                        "Criminal Justice",
                        "Human Rights Law",
                        "Intellectual Property Law"
                };
            case "Sport Science":
                return new String[]{
                        "Exercise Physiology",
                        "Sports Coaching and Performance",
                        "Physical Education and School Sport",
                        "Sports Injury Rehabilitation",
                        "Strength and Conditioning"
                };
            default:
                return new String[0];
        }
    }
}
