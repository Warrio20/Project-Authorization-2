import { FC, useContext, useEffect, useState } from "react";
import "../../styles/form.css"
import { useNavigate } from "react-router-dom";
import { DataContext } from "../../data/DataContext";
import { AxiosError } from "axios";
const ResetPasswordForm: FC = () => {
  const [password, setPassword] = useState<string>('')
  const [confirm, setConfirm] = useState<string>('')
  const [res, setRes] = useState<String>('')
  const [isErr, setIsError] = useState(false);
  const params = new URLSearchParams(window.location.search);
  const token = params.get('token');
  const context = useContext(DataContext);
  const navigate = useNavigate();
  useEffect(() => {
    if (!token) {
      navigate('/', { replace: true });
    }
  }, [token, navigate]);
  return (
    <form>
      <label>Enter your new password</label>
      <input placeholder="New password" type="password" onChange={e => setPassword(e.target.value)} value={password}></input>
      <input placeholder="Repeat your new password" type="password" onChange={e => setConfirm(e.target.value)} value={confirm}></input>
      <div style={{ color: isErr ? "red" : "green" }}>{res}</div>
      <button type="button" onClick={async () => {
        if (password === confirm) {
          if (token != null) {
            try {
              await context?.resetPassword(token, confirm);
              setRes("You successfully changed your password");
              setIsError(false)
              setPassword(""); setConfirm("");
            } catch (e: any) {
              if (e instanceof AxiosError) {
                setIsError(true);
                setRes(e.response?.data?.message);
              }
            }
          };
        } else {
          setRes("You didn't confirm your password");
        }
      }}>Submit</button>
      <div className="loadingDiv" style={{ display: context?.isProcessing ? undefined : "none" }}></div>
    </form>)
}
export default ResetPasswordForm;