import { FC, useContext, useEffect } from "react";
import Header from "./components/bases/Header";
import { Route, Routes } from "react-router-dom";
import Homepage from "./pages/Homepage";
import Register from "./pages/Register";
import Login from "./pages/Login";
import { DataContext} from "./data/DataContext";
import LoadingScreen from "./pages/LoadingScreen";
import ForgotPassword from "./pages/ForgotPassword";
import Unauthorized from "./components/conditionComponents/Unauthorized";
import ResetPassword from "./pages/ResetPassword";
const App:FC = () => {
  const context = useContext(DataContext);
  useEffect(() => {
    if(localStorage.getItem("token")) {
      context?.refresh();
    }
  }, [])
  const isLoading = context?.isLoading;
  const routes = (
    <>
        <Routes>
          <Route path="/" element={<Header />}>
            <Route path="/" element={<Homepage />} />
             <Route path="register" element={<Unauthorized><Register /></Unauthorized>} />
              <Route path="sign-in" element={<Unauthorized><Login /></Unauthorized>} />
             <Route path="forgot-password" element={<Unauthorized><ForgotPassword /></Unauthorized>}/>
            <Route path="*" element={<div>404 NOT FOUND</div>} />
          </Route>
           <Route path="change-password" element={<Unauthorized><ResetPassword /></Unauthorized>} />
        </Routes>
    </>
  )
  return isLoading ? <LoadingScreen/> : routes
}
export default App;