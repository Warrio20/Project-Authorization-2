import { FC } from "react";
import "../styles/form.css"
import BlackContainer from "../components/bases/BlackContainer";
import ForgotPasswordForm from "../components/formComponents/ForgotPassword";
const ForgotPassword:FC = () => {
    return (
    <BlackContainer>
        <ForgotPasswordForm />
    </BlackContainer>
    );
} 
export default ForgotPassword;