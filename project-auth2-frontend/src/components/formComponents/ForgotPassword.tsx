import { FC, useContext, useEffect, useState } from "react";
import "../../styles/form.css"
import { DataContext } from "../../data/DataContext";
import { useNavigate } from "react-router-dom";
import { AxiosError} from "axios";
const ForgotPasswordForm:FC = () => {
    const [email, setEmail] = useState<string>('')
    const context = useContext(DataContext);
    const navigate = useNavigate();
    useEffect(() => {
        if (context?.isAuthorized) {
          navigate('/', { replace: true });
        }
      }, [context?.isAuthorized, navigate]);
    const [isErr, setIsError] = useState(false);
    const [res, setRes] = useState("");
    return (
    <form>
        <label>Enter your email to change your password</label>
        <input placeholder="Email" type="email" onChange={e => setEmail(e.target.value)} value={email}></input>
        <div style={{color: isErr ? "red" : "green" }}>{res}</div>
        <button type="button" onClick={ async () => {
          try {
            await context?.forgotPassword(email); setIsError(false);
            setRes("Check your email to change your password");
            setEmail("");
          } catch (e: any) {
            if(e instanceof AxiosError){
              setIsError(true);
              setRes(e.response?.data?.message);
            }
          }
          }}>Submit</button>
    </form>)
}
export default ForgotPasswordForm;