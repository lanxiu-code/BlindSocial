import registerRead from "./read"; // 获取焦点
export default function registerDirectives(app: any) {
  registerRead(app);
}
