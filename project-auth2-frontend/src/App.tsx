import { FC } from "react";
import Header from "./components/basic/Header";
import { Route, Routes } from "react-router-dom";
import Homepage from "./pages/Homepage";
import Register from "./pages/Register";
import Login from "./pages/Login";
import { DataProvider } from "./data/DataContext";
const App: FC = () => {
  return (
    <>
      <DataProvider>
        <Routes>
          <Route path="/" element={<Header />}>
            <Route path="/" element={<Homepage />} />
            <Route path="/register" element={<Register />} />
            <Route path="sign-in" element={<Login />} />
            <Route path="*" element={<div>404 NOT FOUND</div>} />
          </Route>
        </Routes>
      </DataProvider>
    </>
  )
}
export default App;