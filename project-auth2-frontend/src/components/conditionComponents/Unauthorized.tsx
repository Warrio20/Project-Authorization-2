import { FC, useContext } from "react";
import { Navigate } from "react-router-dom";
import { DataContext } from "../../data/DataContext";
interface RouteProps {
    children: JSX.Element;
  }
const Unauthorized:FC<RouteProps> = ({ children }) => {
    const context = useContext(DataContext);
    if(context?.isAuthorized){
        return <Navigate to="/"/>
    }
    // const navigate = useNavigate();
    // useEffect(() => {
    //     if (context?.isAuthorized) {
    //       navigate('/', { replace: true });
    //     }
    //   }, [context?.isAuthorized, navigate]);
    return children;
}
export default Unauthorized;