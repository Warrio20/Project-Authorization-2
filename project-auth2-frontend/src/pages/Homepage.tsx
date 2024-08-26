import { FC } from "react";
import Title from "../components/bases/Title"
const Homepage: FC = () => {
  const subtitle = "Neque porro quisquam est que dolorem ipsum, quia dolor sit amet consectetur adipsci velit, adipisicing elit. Cumque numquam dolores pariatur amet commodi ipsam iste a aspernatur at eius! Perferendis sed ipsam officia quos impedit, iure blanditiis. Harum, eaque. Lorem ipsum, dolor sit amet consectetur adipisicing elit. Dolore saepe et ducimus, corrupti cupiditate voluptatem eius repellendus? At distinctio temporibus, harum pariatur ducimus vitae soluta nisi iusto, eaque adipisci ipsa.";
  return (
    <>
      <Title title="MY NEW AUTHORIZATION APP" subtitle={subtitle} />
    </>
  )
}
export default Homepage;