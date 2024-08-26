import { FC } from "react";
import BlackContainer from "../components/bases/BlackContainer";
import LoginForm from "../components/formComponents/LoginForm";
const Login:FC = () => {
    return (
      <BlackContainer width="40%">
        <LoginForm />
      </BlackContainer>
    )
}
export default Login