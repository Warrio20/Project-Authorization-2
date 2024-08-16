import { FC } from "react";
import Title from "../components/basic/Title"
const Homepage: FC = () => {
  const subtitle = "Lorem ipsum dolor, sit amet consectetur adipisicing elit. Cumque numquam dolores pariatur amet commodi ipsam iste a aspernatur at eius! Perferendis sed ipsam officia quos impedit, iure blanditiis. Harum, eaque. Lorem ipsum, dolor sit amet consectetur adipisicing elit. Dolore saepe et ducimus, corrupti cupiditate voluptatem eius repellendus? At distinctio temporibus, harum pariatur ducimus vitae soluta nisi iusto, eaque adipisci ipsa.";
  return (
    <>
      <Title title="MY NEW AUTHORIZATION APP" subtitle={subtitle} />
    </>
  )
}
export default Homepage;