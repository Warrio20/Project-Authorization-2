import { FC } from "react";
import BlackContainer from "../components/bases/BlackContainer";
import RegisterForm from "../components/formComponents/RegisterForm";

const Register:FC = () => {
    return (
      <BlackContainer width="40%">
        <RegisterForm />
      </BlackContainer>
    )
}
export default Register