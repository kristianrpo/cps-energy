import "@/styles/globals.css";
import { Space_Grotesk } from "next/font/google";

const spaceGrotesk = Space_Grotesk({
  subsets: ["latin"],
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${spaceGrotesk.className} antialiased my-[20px] mx-[10px] sm:mx-[20px] md:mx-[30px] lg:mx-[40px] xl:mx-[50px] 2xl:mx-[75px] bg-[#111214] text-zinc-200`}
      >
        {children}
      </body>
    </html>
  );
}
